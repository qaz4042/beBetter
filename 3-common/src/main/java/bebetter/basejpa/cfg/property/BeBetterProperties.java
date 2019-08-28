package bebetter.basejpa.cfg.property;

import bebetter.basejpa.constant.C_System;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

/**
 * 读取yml系统级参数
 */
@Component
@ConfigurationProperties(prefix = C_System.OurPackage)
@Data
public class BeBetterProperties {
    CorsConfiguration cors;
    //CorsConfiguration cors = new CorsConfiguration().applyPermitDefaultValues();
    //final Websocket websocket = new Websocket();

    //Async async;

    Boolean isAdmin = false;
    /**
     * websocket配置
     */
    @Data
    static class Websocket {
        boolean enabled = false;
    }

    @Data
    static class Async {
        int corePoolSize = 2;
        int maxPoolSize = 50;
        int queueCapacity = 10000;
    }

    @Data
    public static class Cache {
        J2CacheConfig j2CacheConfig = new J2CacheConfig();

        @Data
        static class J2CacheConfig {
            boolean manager = false;
            String configLocation = "/j2cache.properties";
//            String broadcast = "base.bebetter.framework.config.redis.cache.support.redis.SpringRedisPubSubPolicy";
//            String l2CacheName = "base.bebetter.framework.config.redis.cache.support.redis.SpringRedisProvider";
            /**
             * 是否开启spring cache缓存,注意:开启后需要添加spring.cache.type=游客,将缓存类型设置为none
             */
            Boolean openSpringCache = false;
        }
    }
}
