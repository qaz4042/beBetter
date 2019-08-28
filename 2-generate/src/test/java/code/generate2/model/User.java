/*
package bebetter.generate.model;


import EnumSaverModel;
import GColumn;
import GEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;

import static EnumInputType.text;

@EqualsAndHashCode(callSuper = true)
@Data
@GEntity("用户")
public class User extends EnumSaverModel {
    private static final long serialVersionUID = 1L;
    @GColumn(value = "用户", type = text)
    String name;

    @Type(type = "json")

    Address address;

    @GColumn(value = "描述", type = text)
    String descr;
}
*/
