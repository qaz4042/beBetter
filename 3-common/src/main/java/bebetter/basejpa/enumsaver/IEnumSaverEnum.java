package bebetter.basejpa.enumsaver;

import bebetter.basejpa.util.SpringUtils;

/**
 * 注解填充的表,不设置id和主键,只获取
 *
 * @param <T> 实体类例如 Menu.java  Param.java
 */
public interface IEnumSaverEnum<T extends EnumSaverModel> {
    /**
     * code,不唯一的主键,常常直接用枚举的name
     * 最多只能查出一条有效记录 delete=0只能有一条
     */
    default String getCode() {
        return name();
    }

    String name();

    /**
     * 是否删除  只做逻辑删除
     */
    default Boolean getRemove() {
        return false;
    }

    /**
     * 一个code,最多只能查出一条有效记录. 但code不唯一,也不推荐用无含义的id作code
     */
    default Boolean getModify() {
        return false;
    }

    /**
     * 枚举自身转对象
     */
    Class<? extends IEnumSaverDao<T>> daoClass();

    default IEnumSaverDao<T> getDao() {
        return SpringUtils.getBean(daoClass());
    }
}
