package bebetter.basejpa.cfg.sub;

import cn.hutool.crypto.digest.DigestUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public abstract class IRealm extends AuthorizingRealm {

    /**
     * 密码加密
     *
     * @param password 原文密码
     * @return 密文密码
     */

    public String passwordEncoder(Object password) {
        String passwordStr;
        if (password instanceof String) {
            passwordStr = (String) password;

        } else {
            passwordStr = new String((char[]) password);//Shiro默认是char[]类型的
        }
        return DigestUtil.md5Hex(passwordStr);
    }

    /**
     * 登录&&判断密码是否
     *
     * @param authenticationToken LoginController 请求中的参数
     * @return
     */

    @Override
    abstract protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException;

    /**
     * 填充角色&&权限信息
     */

    @Override
    abstract protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection);
}
