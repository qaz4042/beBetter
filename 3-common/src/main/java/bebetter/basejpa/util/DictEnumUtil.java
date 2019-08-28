package bebetter.basejpa.util;

import bebetter.basejpa.enums.IDictEnum;
import bebetter.basejpa.enums.IEnumColor;
import bebetter.statics.model.KnowException;
import bebetter.statics.util.V;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字典枚举(页面展示可以直接在这枚举里取)
 *
 * @author Administrator
 */
@SuppressWarnings("WeakerAccess")
@Slf4j
public class DictEnumUtil {

    /**
     * 枚举label【界面展示】
     */
    private static final String FIELD_LABEL = "label";

    /**
     * 获取字典
     *
     * @return Map(name, label)
     * label>  例如 {INIT:'初始化',SUCCESS:'成功'}
     */
    @SneakyThrows
    public static <T extends Enum> Map<String, String> getNameLabelMap(Class<T> eClass) {
        Map<String, String> map = new LinkedHashMap<>(16);
        try {
            for (T t : enumList2(eClass)) {
                map.put(getCode(t), getLabel(t));
            }
        } catch (NoSuchFieldException e) {
            for (T t : enumList2(eClass)) {
                map.put(getCode(t), null);
            }
            //log.info(eClass.getSimpleName() + ".class没有定义" + FIELD_LABEL + "属性,也没实现IDictEnum接口,将获取不到枚举字典" + JSONUtil.toJsonStr(map));
        }
        return map;
    }

    private static <T> String getCode(T t) {
        if (bebetter.basejpa.enums.IDictEnum.class.isAssignableFrom(t.getClass())) {
            return ((bebetter.basejpa.enums.IDictEnum) t).getCode();
        }
        return ((Enum) t).name();
    }

    private static <T> String getLabel(T t) throws NoSuchFieldException, IllegalAccessException {
        if (bebetter.basejpa.enums.IDictEnum.class.isAssignableFrom(t.getClass())) {
            return ((IDictEnum) t).getLabel();
        }
        Field label = t.getClass().getDeclaredField(DictEnumUtil.FIELD_LABEL);
        label.setAccessible(true);
        return (String) label.get(t);
    }


    public static <T extends Enum<T>> T fromString(Class<T> clazz, String name) {
        if (V.empty(name)) {
            return null;
        }
        return Enum.valueOf(clazz, name);
    }

    /**
     * 取枚举所有项的每个字段,name和label字段组成 Map<name,LABEL>
     *
     * @return 例如 [{name:'INIT',LABEL:'初始化',color:'blue'},{name:'SUCCESS',LABEL:'成功',color:'green'}]
     */
    public static <T extends Enum<T>> List<Map<String, ?>> getFullList(Class<T> eClass) {
        return enumList(eClass).stream().map(DictEnumUtil::toMap).collect(Collectors.toList());
    }


    @SneakyThrows
    public static <T extends Enum<T>> List<T> enumList(Class<T> clazz) {
        try {
            return new ArrayList<>(EnumSet.allOf(clazz));
        } catch (ClassCastException e) {
            log.error("Enum.enumList()异常|" + e.getMessage() + "|className=" + clazz);
            return new ArrayList<>(0);
        }
    }

    @SneakyThrows
    public static <T> List<T> enumList2(Class<T> clazz) {
        if (null == clazz || !clazz.isEnum()) {
            throw new KnowException("无法获取枚举字典,无效的枚举类=" + clazz);
        }
        //noinspection unchecked
        return enumList((Class) clazz);
    }


    /**
     * 【jdk内部】枚举name()【存入库】
     * 【jdk内部】枚举变量$VALUES,即values()返回的内容
     * 【jdk内部】枚举values()
     */
    private static final String FIELD_NAME = "name";
    private static final String FIELD_VALUES = "$VALUES";
    private static final String METHOD_VALUES = "values";

    /**
     * 枚举实例 转
     *
     * @param t 枚举实例                例如,EnumExample.INIT
     * @return Map(propName, values)    例如,{name:'INIT',label:'初始化',color:'blue'}
     */
    @SneakyThrows
    public static <T extends Enum> Map<String, Object> toMap(T t) {
        Map<String, Object> map = new HashMap<>(16);
        Class<?> aClass = t.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            if (!field.isEnumConstant() && !FIELD_VALUES.equals(field.getName())) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                map.put(field.getName(), field.get(t));
            }
        }
        if (IEnumColor.class.isAssignableFrom(aClass)) {
            IEnumColor t2 = (IEnumColor) t;
            map.put("style", t2.getStyle());
        }
        map.computeIfAbsent("code", k -> t.name());
        map.computeIfAbsent("label", k -> t.name());
        return map;
    }
}
