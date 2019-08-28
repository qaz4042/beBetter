package bebetter.basejpa.cfg;


import cn.hutool.core.collection.ConcurrentHashSet;
import bebetter.basejpa.cfg.property.BeBetterProperties;
import bebetter.basejpa.cfg.sub.MyPrincipalHandshakeHandler;
import bebetter.basejpa.enums.ISysRole;
import bebetter.basejpa.model.base.BaseUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocketMessageBroker
//@EnableWebSocket
@Slf4j
public class CfgWebSocket implements WebSocketMessageBrokerConfigurer {
    //已登录用户
    public static Map<ISysRole, Map<Long, BaseUser>> loginMap = new ConcurrentHashMap<>(8);
    //所有连接数 (包括登录和未登录用户)
    public static Set<String> sessionIdSet = new ConcurrentHashSet<>(2048);

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public @NonNull
            WebSocketHandler decorate(@NonNull WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
                        BaseUser user = (BaseUser) session.getPrincipal();
                        if (null != user) {
                            ISysRole sysRole = user.getSysRole();
                            Map<Long, BaseUser> map = loginMap.get(sysRole);
                            if (null == map) {
                                map = new ConcurrentHashMap<>(2048);
                                loginMap.put(sysRole, map);
                            }
                            map.put(user.getId(), user);
                        }
                        sessionIdSet.add(session.getId());
                        super.afterConnectionEstablished(session);
                    }

                    @Override
                    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
                        BaseUser user = (BaseUser) session.getPrincipal();
                        if (null != user) {
                            loginMap.get(user.getSysRole()).remove(user.getId());//可能是用户(user2) 也可能是后台代理(agenet)  admin端会是admin
                        }
                        sessionIdSet.remove(session.getId());
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });
    }

    @Autowired
    private BeBetterProperties codeProperties;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("");
        config.setUserDestinationPrefix("/user2");//convertAndSendToUser时,第二个参数destination,加上这个UserDestinationPrefix开头=js端
//        config.setApplicationDestinationPrefixes("/app");
    }

    String Websocket_EndPoint = "/websocket";

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] allowedOrigins = Optional.ofNullable(codeProperties.getCors().getAllowedOrigins()).map(origins -> origins.toArray(new String[]{})).orElse(new String[0]);
        registry.addEndpoint(Websocket_EndPoint)
                .setAllowedOrigins(allowedOrigins)
                .setHandshakeHandler(new MyPrincipalHandshakeHandler())
                .withSockJS();
    }
}
