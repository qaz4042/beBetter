package bebetter.basejpa.model.base;

import bebetter.basejpa.enums.ISysRole;
import bebetter.generate.annotation.GColumn;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.security.Principal;

/**
 * 基本用户模型
 */
@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class BaseUser extends Id_CreateTime implements Principal {
    @Id
    Long id;

    @GColumn("用户名")
    String username;

    @GColumn("密码")
    transient String password;// 密码   在转为json字符串时(包括输出页面)被隐藏

    @GColumn("状态")
    Boolean enabled;// 是否可登录    true=1=可以,false=0=不可

    @Override
    public String getName() {
        return username;
    }

    public abstract ISysRole getSysRole();
}
