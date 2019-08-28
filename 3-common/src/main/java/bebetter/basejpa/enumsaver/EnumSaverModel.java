package bebetter.basejpa.enumsaver;

import bebetter.basejpa.model.base.Id_CreateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 注解填充        model必须继承
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public class EnumSaverModel extends Id_CreateTime{
    //通过代码逻辑限制,一个code只能有一个enable的状态
    String code;

    //是否有效
    Boolean enable;
    //时间
    LocalDateTime disableTime;
}
