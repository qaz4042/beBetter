package bebetter.basejpa.cfg.sub;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class ToSocket implements Serializable {
    private static final long serialVersionUID = 3110367564503529058L;

    public ToSocket() {
    }

    public ToSocket(String url, String externMessage, Serializable... userIds) {
        this.isAllUser = false;
        this.url = url;
        this.externMessage = externMessage;
        this.userIds = Arrays.asList(userIds);
    }

    /**
     * @param url 例如"/getUserMoney"  java中调用不用带/user2  js端订阅需多拼接/user2
     */
    public ToSocket(String url, String externMessage, List<Serializable> userIds) {
        this.isAllUser = false;
        this.url = url;
        this.externMessage = externMessage;
        this.userIds = userIds;
    }

    public ToSocket(Boolean isAllUser, String url, String externMessage) {
        this.isAllUser = isAllUser;
        this.url = url;
        this.externMessage = externMessage;
        this.userIds = null;
    }

    Boolean isAllUser;
    String url;
    String externMessage;
    List<Serializable> userIds;
}
