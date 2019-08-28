package bebetter.basejpa.model.db;

import bebetter.basejpa.enumsaver.EnumSaverModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Data
//@Accessors
@Entity
public class Param extends EnumSaverModel {
    String value;       //
    String parentCode;       //
    String title;       //参数名称
    String description; //详细说明参数的 默认值/取值范围 等等
    Boolean isAdmin;    //是否是admin才能展示修改
}
