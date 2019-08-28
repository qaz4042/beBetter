package bebetter.basejpa.util;

import bebetter.basejpa.cfg.ICfgRedisMessageDefault;
import bebetter.basejpa.cfg.sub.ToSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 通过redis广播消息
 */
@Service
public class RedisUtil {

    private static RedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    public static void toSocket(ToSocket param){
        redisTemplate.convertAndSend(ICfgRedisMessageDefault.defaultSocket, param);
    }
}
