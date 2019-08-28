package bebetter.basejpa.util;

import bebetter.statics.util.V;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ClassUtil {
    public static Class getArguFromIntf(Class clazz, Class intfClazz) {
        Type[] interfacesTypes = clazz.getGenericInterfaces();
        for (Type t : interfacesTypes) {
            if (t.getTypeName().contains(intfClazz.getTypeName())) {
                Type[] genericType2 = ((ParameterizedType) t).getActualTypeArguments();
                if (V.noEmpty(genericType2)) {
                    return (Class) genericType2[0];
                }
            }
        }
        return null;
    }


    @SneakyThrows
    public static <T> T anyInstance(Class<T> numClass) {
        if (numClass.isEnum()) {
            return DictEnumUtil.enumList2(numClass).get(0);
        }
        return numClass.newInstance();
    }

    public static List<Field> fields(Class c) {
        return Arrays.stream(c.getDeclaredFields())
                .filter(f -> !Modifier.isFinal(f.getModifiers())).collect(Collectors.toList());
    }

    public static <F> List<F> props(Object o, BiFunction<Field, Object, F> function) {
        //noinspection unchecked
        List<F> list = Collections.EMPTY_LIST;
        if (null != o) {
            list = fields(o.getClass()).stream().map(field -> {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Object value = field.get(o);
                    return function.apply(field, value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        }

        return list;
    }
}
