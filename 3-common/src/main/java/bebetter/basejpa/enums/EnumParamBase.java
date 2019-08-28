package bebetter.basejpa.enums;

import bebetter.basejpa.dao.ParamDao;
import bebetter.basejpa.enumsaver.IEnumSaverDao;
import bebetter.basejpa.enumsaver.IEnumSaverEnum;
import bebetter.basejpa.model.db.Param;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumParamBase implements IEnumSaverEnum<Param>, IEnumParam {

    //code      | values   | parentCode|  title  |        descr        |isAdmin| remove | modify
    前端地址("http://localhost:8083", null, "前端地址", "逗号隔开", true, false, false, false),
    ;

    //
    String value;
    String parentCode;
    String title;
    String description;
    Boolean isAdmin;//是否是admin才能展示编辑
    Boolean isShow;//是否是admin才能展示编辑


    Boolean remove;
    Boolean modify;

    @Override
    public Class<? extends IEnumSaverDao<Param>> daoClass() {
        return ParamDao.class;
    }

    @Override
    public Boolean getIsShow() {
        return isShow;
    }
}
