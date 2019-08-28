package bebetter.basejpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumSysRoleBase implements ISysRole {
//    游客,
    用户,
    平台代理,
    管理员,
    ;
}
