package bebetter.basejpa.cfg.sub;

import bebetter.basejpa.cfg.sub.ToSocket;
import bebetter.basejpa.util.SocketUtil;
import lombok.SneakyThrows;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.lang.NonNull;

public class SimpleWebSocketRedisMessageListener implements MessageListener {
    private static GenericJackson2JsonRedisSerializer jackson2Serializer = new GenericJackson2JsonRedisSerializer();

    /**
     * 1.通过redis广播到各个订阅端(game-user2)
     * 2.各个订阅端(game-user2)发送socket消息给客户浏览器
     */
    @Override
    @SneakyThrows
    public void onMessage(@NonNull Message message, byte[] pattern) {
        SocketUtil.socketSend(jackson2Serializer.deserialize(message.getBody(), ToSocket.class));
    }
}
