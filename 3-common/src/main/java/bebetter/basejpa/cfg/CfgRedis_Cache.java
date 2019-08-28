package bebetter.basejpa.cfg;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;

/**
 * redis配置  由于用了j2cache框架,redis容器基本依赖J2cache初始的.
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
@ConditionalOnProperty(value = "j2cache.L2.provider_class", havingValue = "redis")
public class CfgRedis_Cache {
    public static final String J2CacheRedisConnectionFactory = "j2CahceRedisConnectionFactory";

    /**
     * 只配了j2cache的redis的host/password/...参数,统一用它,否则连不上(例如actuator监控时要检测是否连接正常 会调用name=redisConnectionFactory的bean)
     */
    @Bean("redisConnectionFactory")
    RedisConnectionFactory redisConnectionFactory(@Qualifier(CfgRedis_Cache.J2CacheRedisConnectionFactory) RedisConnectionFactory connectionFactory) {
        return connectionFactory;
    }


    /**
     * Gets the redis template.
     *
     * @param connectionFactory the jedis connection factory
     * @return the redis template
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Serializable> getRedisTemplate(@Qualifier(CfgRedis_Cache.J2CacheRedisConnectionFactory) RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        // 设置值序列化为Jackson
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        // 解决同类型不同Classloader导致CastClassException问题
        template.setBeanClassLoader(this.getClass().getClassLoader());
        return template;
    }
}
