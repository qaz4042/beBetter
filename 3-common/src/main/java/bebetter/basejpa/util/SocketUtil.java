package bebetter.basejpa.util;

import bebetter.basejpa.cfg.sub.ToSocket;
import bebetter.basejpa.enums.EnumSysRoleBase;
import bebetter.statics.util.V;

import java.io.Serializable;

@SuppressWarnings("WeakerAccess")
public class SocketUtil extends BaseSocketUtil {

    /***
     * 2.各个订阅端(game-user2)发送socket消息给客户浏览器
     */
    public static void socketSend(ToSocket param) {
        if (null != param) {
            if (V.isTrue(param.getIsAllUser())) {
                socketSendUserAll(param.getUrl(), param.getExternMessage());
            } else {
                socketSendUser(param.getUrl(), param.getExternMessage(), param.getUserIds().toArray(new Serializable[]{}));
            }
        }
    }

    /***
     * 发送消息给特定用户
     * @param url       例如 "/getLastPeriods"    服务器发送时,不用统一开头"/user2"  js订阅时需要"/user2"开头
     * @param message   例如  "我是李白"
     * @param userIds    例如  唯一用户表示, 对比webSession中的principle的username  EnumSysRoleBase.user2.name()+ "_" + "1"
     */
    public static void socketSendUser(String url, String message, Serializable... userIds) {
        for (Serializable userId : userIds) {
            BaseSocketUtil.sendToUser(EnumSysRoleBase.用户 + "_" + userId, url, message);
        }
    }

    /***
     * 发送消息给所有用户
     * @param url       例如 "/getLastPeriods"    服务器发送时,不用统一开头"/user2"  js订阅时需要"/user2"开头
     * @param message   例如  "最新期时3,3,5"
     */
    public static void socketSendUserAll(String url, String message) {
        BaseSocketUtil.sendUserAll(url, message);
    }
}
