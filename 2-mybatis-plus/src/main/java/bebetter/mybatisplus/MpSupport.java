package bebetter.mybatisplus;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @date 2019-04-17
 */
@Slf4j
public class MpSupport {

    /**
     * ID字段名
     */
    public static final String ID_COLUMN = "id";

    public static final String LIKE = "#%#";

    /**
     * 大于
     */
    public static final String GT = "#>#";

    /**
     * 大于等于 &gt;=
     */
    public static final String GE = "#>=#";

    /**
     * 小于
     */
    public static final String LT = "#<#";

    /**
     * 小于等于 &gt;=
     */
    public static final String LE = "#<=#";

    /**
     * 小于等于 &gt;=
     */
    public static final String IN = "#in#";

    /**
     * 创建时间字段名
     */
    public static final String CREATE_TIME_COLUMN = "createTime";

    /**
     * 创建日期字段
     */
    public static final String CREATE_DATE_COLUMN = "createDate";

    /**
     * 更新时间字段名
     */
    public static final String UPDATE_TIME_COLUMN = "updateTime";


    public static <T> Page<T> getPage(PageQuery param) {
        if (null == param) {
            return new Page<>();
        }
        Integer currentPage = param.getCurrentPage();
        Integer pageSize = param.getPageSize();
        return new Page<>(currentPage, pageSize);
    }

    public static <T> Page<T> getPage(Map<String, Object> param) {
        if (null == param) {
            return new Page<>();
        }
        Integer currentPage = MapUtil.getInt(param, "currentPage");
        Integer pageSize = MapUtil.getInt(param, "pageSize");
        Page<T> page = new Page<>(null == currentPage ? 1 : currentPage, null == pageSize ? 10 : pageSize);
        String asc = MapUtil.getStr(param, "asc");
        String desc = MapUtil.getStr(param, "desc");
        if (StrUtil.isNotBlank(asc)) {
            for (String s : StrUtil.splitToArray(asc, StrUtil.C_COMMA)) {
                page.addOrder(OrderItem.asc(StrUtil.toUnderlineCase(s)));
            }
        }
        if (StrUtil.isNotBlank(desc)) {
            for (String s : StrUtil.splitToArray(desc, StrUtil.C_COMMA)) {
                page.addOrder(OrderItem.desc(StrUtil.toUnderlineCase(s)));
            }
        }
        return page;
    }

    /**
     * 获取MP查询对象
     *
     * @param <T>
     * @param entity
     * @param clazz
     * @return
     */
    public static <T> QueryWrapper<T> getQueryWrapper(T entity, Class<?> clazz) {
        return Wrappers.query(entity);
    }

    /**
     * 解析,对象属性值内,包含模糊查询等表达式,作为查询条件
     */
    @SneakyThrows
    public static <T> QueryWrapper<T> getQueryWrapperMore(T entity) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        for (Method method : entity.getClass().getDeclaredMethods()) {
            String methodName = method.getName();
            if (methodName.startsWith("get") && ArrayUtil.isEmpty(method.getParameterTypes())) {
                String fieldName = StrUtil.toUnderlineCase(methodName.substring(3));
                Object val = method.invoke(entity);
                if (val instanceof String) {
                    String vStr = (String) val;
                    if (vStr.contains(LIKE)) {
                        qw.like(fieldName, StrUtil.replace(vStr, LIKE, StrUtil.EMPTY));
                    } else if (vStr.contains(GT)) {
                        qw.gt(fieldName, StrUtil.replace(vStr, GT, StrUtil.EMPTY));
                    } else if (vStr.contains(GE)) {
                        qw.ge(fieldName, StrUtil.replace(vStr, GE, StrUtil.EMPTY));
                    } else if (vStr.contains(LT)) {
                        qw.lt(fieldName, StrUtil.replace(vStr, LT, StrUtil.EMPTY));
                    } else if (vStr.contains(LE)) {
                        qw.le(fieldName, StrUtil.replace(vStr, LE, StrUtil.EMPTY));
                    } else {
                        qw.eq(fieldName, vStr);
                    }
                } else if (null != val) {
                    qw.eq(fieldName, val);
                }
            }
//            else {
//                log.info("where {} = ? 没有拼接到sql,因为属性特殊,不可读", methodName);
//            }
        }
        return qw;
    }

    public static <DTO, T> QueryWrapper<T> getQueryWrapper(Map<String, Object> param, Class<DTO> clazz) {
        QueryWrapper<T> qw = new QueryWrapper<>();
        if (null == param) {
            //TODO throw ex
            return qw;
        }
        List<String> fieldList = Arrays.stream(ClassUtil.getDeclaredFields(clazz)).map(Field::getName).collect(Collectors.toList());
        List<String> supperFieldList = Arrays.stream(ClassUtil.getDeclaredFields(clazz.getSuperclass())).map(Field::getName).collect(Collectors.toList());
        fieldList.addAll(supperFieldList);
        //qw.setEntity(BeanUtils.instantiateClass(clazz));
        if (CollectionUtil.isNotEmpty(param)) {
            param.forEach((k, v) -> {
                String[] split = k.split("\\.");
                String filed = split.length == 2 ? split[1] : split[0];
                String appendBefore = split.length == 2 ? split[0] + "." : "";
                if (!ObjectUtils.isEmpty(v) && fieldList.contains(filed)) {
                    filed = StrUtil.toUnderlineCase(filed);
                    String vStr = v.toString();
                    String column = appendBefore + filed;
                    if (vStr.contains(LIKE)) {
                        qw.like(column, StrUtil.replace(vStr, LIKE, StrUtil.EMPTY));
                    } else if (vStr.contains(GT)) {
                        qw.gt(column, StrUtil.replace(vStr, GT, StrUtil.EMPTY));
                    } else if (vStr.contains(GE)) {
                        qw.ge(column, StrUtil.replace(vStr, GE, StrUtil.EMPTY));
                    } else if (vStr.contains(LT)) {
                        qw.lt(column, StrUtil.replace(vStr, LT, StrUtil.EMPTY));
                    } else if (vStr.contains(LE)) {
                        qw.le(column, StrUtil.replace(vStr, LE, StrUtil.EMPTY));
                    } else if (vStr.contains(IN)) {
                        qw.in(column, (Object[]) StrUtil.replace(vStr, IN, StrUtil.EMPTY).split(","));
                    } else {
                        qw.eq(column, v);
                    }
                }
            });
        }

        if (!param.containsKey("desc") && !param.containsKey("asc") && GeneralEntity.class.isAssignableFrom(clazz)) {
            qw.orderByDesc(MpSupport.ID_COLUMN);
        }
        return qw;
    }


    public static <T> String columnToString(SFunction<T, ?> column) {
        return StrUtil.toCamelCase(getColumn(LambdaUtils.resolve(column), true));
    }

    private static Map<String, ColumnCache> columnMap = null;
    private static boolean initColumnMap = false;

    /**
     * 获取 SerializedLambda 对应的列信息，从 lambda 表达式中推测实体类
     * <p>
     * 如果获取不到列信息，那么本次条件组装将会失败
     *
     * @param lambda     lambda 表达式
     * @param onlyColumn 如果是，结果: "name", 如果否： "name" as "name"
     * @return 列
     * @throws com.baomidou.mybatisplus.core.exceptions.MybatisPlusException 获取不到列信息时抛出异常
     * @see SerializedLambda#getImplClass()
     * @see SerializedLambda#getImplMethodName()
     */
    private static String getColumn(SerializedLambda lambda, boolean onlyColumn) throws MybatisPlusException {
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        Class<?> aClass = lambda.getInstantiatedType();
        if (!initColumnMap) {
            columnMap = LambdaUtils.getColumnMap(aClass);
            initColumnMap = true;
        }
        Assert.notNull(columnMap, "can not find lambda cache for this entity [%s]", aClass.getName());
        ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
        Assert.notNull(columnCache, "can not find lambda cache for this property [%s] of entity [%s]",
                fieldName, aClass.getName());
        return onlyColumn ? columnCache.getColumn() : columnCache.getColumnSelect();
    }
}
