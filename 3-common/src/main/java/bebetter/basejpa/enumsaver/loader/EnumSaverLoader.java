package bebetter.basejpa.enumsaver.loader;

import bebetter.basejpa.cfg.BaseMain;
import bebetter.basejpa.enumsaver.EnumSaverModel;
import bebetter.basejpa.enumsaver.IEnumSaverDao;
import bebetter.basejpa.enumsaver.IEnumSaverEnum;
import bebetter.basejpa.util.CollectionUtil;
import bebetter.basejpa.util.DictEnumUtil;
import bebetter.mybatisplus.model.Cond;
import bebetter.statics.util.V;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLSyntaxErrorException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
@DependsOn("springUtils")
public class EnumSaverLoader {

    @PostConstruct
    public void load() {
        //扫描项目中的枚举们
        Set<Class<? extends bebetter.basejpa.enumsaver.IEnumSaverEnum>> clazzSet = BaseMain.getSubTypesOf(bebetter.basejpa.enumsaver.IEnumSaverEnum.class);
        clazzSet.forEach(clazz -> {
            try {
                //noinspection unchecked
                enum2Db(clazz);
            } catch (Exception e) {
                Throwable cause = e.getCause();
                if (cause instanceof SQLSyntaxErrorException) {
                    log.warn("枚举保存器{}异常|{}", clazz, cause.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    private static <T extends bebetter.basejpa.enumsaver.EnumSaverModel, F extends IEnumSaverEnum<T>> void enum2Db(Class<F> clazz) {
        int deleteSize = 0, modifySize = 0, addSize = 0;
        List<F> enums = DictEnumUtil.enumList2(clazz);

        if (V.empty(enums)) {
            return;
        }
        IEnumSaverDao<T> dao = enums.get(0).getDao();
        Class<T> modelClass = getModelClass(enums.get(0).daoClass());


        List<T> deleteList = new ArrayList<>(128);
        List<T> insertList = new ArrayList<>(256);
        List<T> exists = dao.list(new Cond<>());
        Map<String, T> existMap = CollectionUtil.listToMap(exists, T::getCode);
        Set<String> codeExists = existMap.keySet();
        //枚举的每行数据
        for (F enu : enums) {
            //枚举转实体
            T entity = dao.tranTo(enu, modelClass);

            String code = entity.getCode();
            boolean exist = codeExists.contains(code);

            boolean remove = null == enu.getRemove() ? false : enu.getRemove();
            boolean modify = null == enu.getModify() ? false : enu.getModify();

            if (exist) {
                if (remove) {
                    deleteSize++;
                    deleteList.add(entity);
                } else {
                    if (modify) {
                        T existOne = existMap.get(code);
                        if (!isEqual((Enum) enu, existOne)) {
                            modifySize++;
                            deleteList.add(entity);
                            insertList.add(entity);
                        }
                    }
                }
            }
            //不存在就添加
            else {
                if (!remove) {
                    addSize++;
                    insertList.add(entity);
                }
            }
        }
        LocalDateTime now = LocalDateTime.now();

        deleteList.forEach(e -> {
            e.setDisableTime(now);
            e.setEnable(false);
        });

        insertList.forEach(e -> {
            e.setCreateTime(now);
            e.setEnable(true);
        });

        dao.saveAll(deleteList);
        dao.saveAll(insertList);

        if (deleteSize + modifySize + addSize > 0) {
            log.info("枚举保存器[{}],软删除{}个,修改{}个,新增{}个|deleteTime和createTime={}", clazz.getSimpleName(), deleteSize, modifySize, addSize, now);
        }
        if (modifySize > 5) {
            log.warn("枚举保存器[{}],modify=true({}个),会强行用枚举信息覆盖表格,请用完及时设置回false|deleteTime和createTime={}", clazz.getSimpleName(), modifySize, now);
        }
    }

    private static Set<String> enumExcludeProps = CollUtil.newHashSet("remove", "modify");

    @SneakyThrows
    private static <T extends bebetter.basejpa.enumsaver.EnumSaverModel> boolean isEqual(Enum enu1, T existOne) {
        Map<String, Object> enuMap = DictEnumUtil.toMap(enu1);
        for (String fieldName : enuMap.keySet()) {
            if (!enumExcludeProps.contains(fieldName)) {
                Object existProp = BeanUtil.getProperty(existOne, fieldName);
                Object enuProp = enuMap.get(fieldName);
                if (!ObjectUtil.equal(existProp, enuProp)) {
                    return false;
                }
            }
        }
        return true;
    }

    static <T extends EnumSaverModel> Class<T> getModelClass(Class<? extends IEnumSaverDao<T>> daoClass) {
        ParameterizedType entityClass =
                Arrays.stream(daoClass.getGenericInterfaces())
                        .filter(o -> o instanceof ParameterizedType)
                        .map(o -> (ParameterizedType) o)
                        .filter(o -> o.getRawType().equals(IEnumSaverDao.class))
                        .findAny().orElse(null);
        //noinspection unchecked
        return (Class<T>) entityClass.getActualTypeArguments()[0];
    }
}
