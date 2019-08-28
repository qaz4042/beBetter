package bebetter.basejpa.util;

import bebetter.basejpa.util.SpringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

public class BaseRedisMessageUtil {

    public static final String simpleWebSocket = "sendSimpleSocket";

    /**
     * 通过redis广播消息(经常是game-task端广播,game-user中订阅)
     */
    public static void sendSimpleSocket(String message) {
        SpringUtils.getBean(StringRedisTemplate.class).convertAndSend(simpleWebSocket, message);
    }
}
