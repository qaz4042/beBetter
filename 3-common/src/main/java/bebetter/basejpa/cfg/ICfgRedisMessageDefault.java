package bebetter.basejpa.cfg;

import bebetter.basejpa.cfg.CfgRedis_Cache;
import bebetter.basejpa.cfg.sub.SimpleWebSocketRedisMessageListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

//@Configuration  请在订阅端去开启,例如game-user端添加实现类以及@Configuration注解
public interface ICfgRedisMessageDefault {
    String defaultSocket = "defaultSocket";

    @Bean
    default RedisMessageListenerContainer container(@Qualifier(CfgRedis_Cache.J2CacheRedisConnectionFactory) RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter(), new PatternTopic(defaultSocket));
        return container;
    }

    @Bean
    default MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter(new SimpleWebSocketRedisMessageListener());
//        (new SendSimpleSocket_receiver(), BaseRedisMessageUtil.simple_method);
    }
}
