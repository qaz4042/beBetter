package bebetter.basejpa.enumsaver;

import bebetter.mybatisplus.base.BaseDao;
import bebetter.mybatisplus.model.Cond;
import cn.hutool.core.bean.BeanUtil;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 注解填充实现接口
 * 没有logic效果了!! (EnumSaverModel不使用delete字段的逻辑删除效果)
 *
 * @param <T> model类
 */
public interface IEnumSaverDao<T extends EnumSaverModel> extends BaseDao<T> {
    @SneakyThrows
    default T tranTo(IEnumSaverEnum enu, Class<T> modelClass) {
        T o = modelClass.newInstance();
        BeanUtil.copyProperties(enu, o);
        o.setCode(enu.getCode());
        return o;
    }

    default T getByCode(String code) {
        return one(new Cond<T>().eq(T::getCode, code).eq(T::getEnable, true));
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型
     *
     * @param clazz
     */
    public static Class getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class) params[index];
    }

    default List<T> list() {
        return list(new Cond<T>().eq(T::getEnable, true).select(T::getCode));
    }
}
