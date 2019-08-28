package bebetter.basejpa.util;

import bebetter.basejpa.util.SpringUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class BaseSocketUtil {
    /***
     *
     * @param url       例如 "/getLastPeriods"
     * @param message   例如  "我是李白"
     */
    public static void sendUserAll(String url, String message) {
        bebetter.basejpa.util.SpringUtils.getBean(SimpMessagingTemplate.class).convertAndSend(url, message);
    }

    /***
     *
     * @param principleUsername 例如  唯一用户表示, 对比webSession中的principle的username  EnumSysRoleBase.user2.name()+ "_" + "1"
     * @param url       例如 "/getLastPeriods"    服务器发送时,不用统一开头"/user2"  js订阅时需要"/user2"开头
     * @param message   例如  "我是李白"
     */
    public static void sendToUser(String principleUsername, String url, String message) {
        SpringUtils.getBean(SimpMessagingTemplate.class).convertAndSendToUser(principleUsername, url, message);
    }
}
