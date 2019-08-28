package bebetter.basejpa.cfg.sub;/*
package bebetter.system.base.config.sub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public interface CommonMessageListener extends MessageListener {
    @Override
    default void onMessage(Message message, byte[] channelUrlByte) {
        String channelUrl = new String(channelUrlByte);
//        Class<?> bodyClass = BaseMain.UrlEnumMap.get(channelUrl);
//        Object param = JSONUtil.toBean(new String(message.getBody()), bodyClass);
        onMessage(message.getBody(), channelUrl);
    }

    void onMessage(Object param, String channelUrl);
}
*/
