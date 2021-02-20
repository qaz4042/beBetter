package bebetter.demo.entity;

import bebetter.demo.enums.SexEnum;
import bebetter.generate.annotation.GColumn;
import bebetter.generate.annotation.GEntity;
import bebetter.mybatisplus.GeneralEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author LiZuBin
 * @date 2021/2/20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@GEntity
public class User extends GeneralEntity {
    //    private Long id;
    @GColumn("用户名")
    private String username;
    @GColumn("性别")
    private SexEnum sex;

}
