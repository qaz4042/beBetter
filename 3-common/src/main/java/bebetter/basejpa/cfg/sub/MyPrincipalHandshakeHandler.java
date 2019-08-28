package bebetter.basejpa.cfg.sub;

import cn.hutool.core.util.ObjectUtil;
import bebetter.basejpa.model.base.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
public class MyPrincipalHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        /*if (request instanceof ServletServerHttpRequest) {
            Admin admin = LoginUtil.getAdminFromShiro(request.getPrincipal());
            log.info("websocket登录成功|{}", admin);
            return admin;
        }*/
        BaseUser principal = (BaseUser) request.getPrincipal();
        if (null != principal) {
            principal = ObjectUtil.clone(principal);
            //定制username,因为socket要区分后台代理还是普通用户
            principal.setUsername(principal.getSysRole().name() + "_" + principal.getId());
        }
        return principal;
    }
}
