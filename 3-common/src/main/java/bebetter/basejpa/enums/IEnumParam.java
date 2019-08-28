package bebetter.basejpa.enums;

import bebetter.basejpa.dao.ParamDao;
import bebetter.basejpa.model.db.Param;
import bebetter.basejpa.util.SpringUtils;

/**
 * 菜单枚举必备字段
 */
public interface IEnumParam {
    String name();

    String getValue();

    String getParentCode();

    String getDescription();

    /**
     * 是否是开发人员才可修改
     */
    default Boolean getIsDevelop() {
        return false;
    }

    /**
     * ui-admin 是否展示
     */
    Boolean getIsShow();


    /**
     * 获取值
     */
    default String getValueCache() {
        Param param = SpringUtils.getBean(ParamDao.class).getByCode(name());
        if (null == param) {
            return null;
        }
        return param.getValue();
    }
}
