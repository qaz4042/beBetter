package bebetter.basejpa.cfg;

import cn.hutool.core.util.ClassUtil;
import bebetter.basejpa.util.DictEnumUtil;
import bebetter.statics.util.V;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//import org.reflections.Reflections;

@Slf4j
public class BaseMain {

    //检出子类 (接口的/父类的)
//    public static Reflections SubTypeReflections = new Reflections(C_System.OurPackage, new SubTypesScanner());

//   public static final Reflections reflections = new Reflections("code");

    /**
     * 获取枚举接口,的所有枚举类,的所有枚举项
     */
    public static <T> List<T> getImplEnumsByIntf(Class<T> clazz) {
        //noinspection unchecked
        return getImplEnumsByEnum((Class) clazz);
    }

    /**
     * 获取接口的所有实现类,的所有枚举项
     */
    private static <T extends Enum<T>> List<T> getImplEnumsByEnum(Class<T> clazz) {
        Set<Class<? extends T>> subClazzs = getSubTypesOf(clazz);
        errorLog(clazz, subClazzs);
        //noinspection unchecked
        return subClazzs.stream().filter(Class::isEnum).flatMap(subClazz -> DictEnumUtil.enumList2((Class<T>) subClazz).stream()).collect(Collectors.toList());
    }

    /*
        public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> className) {
            return reflections.getSubTypesOf(className);
        }*/
    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> clazz) {
        //noinspection unchecked
        return (Set) ClassUtil.scanPackageBySuper("bebetter", clazz);
//        Set<Class<? extends T>> set = new HashSet<>(32);
//        for (T t : ServiceLoader.load(className)) {
//            //noinspection unchecked
//            set.add((Class<? extends T>) t.getClass());
//        }
//        return set;
    }

    private static <T> void errorLog(Class<T> clazz, Set<Class<? extends T>> subClazzs) {
        String errorClassNames = subClazzs.stream().filter(subClazz -> !subClazz.isInterface() && !Enum.class.isAssignableFrom(subClazz))
                .map(Class::getName).collect(Collectors.joining("\n"));
        V.noEmpty(errorClassNames, o -> {
            log.error("error|BaseMain.getEnumsFromAllImpl调用异常|{}的子实现类[{}]应该是枚举,但却不是.", clazz, o);
            return null;
        });
    }
}
