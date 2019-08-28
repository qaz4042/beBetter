package bebetter.basejpa.util;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {
    public static String get(String url, int timeout, Object... errorExternMsg) {
        String result = null;
        try {
            result = cn.hutool.http.HttpUtil.get(url, timeout);
        } catch (Exception e) {
            log.warn("HttpUtil.get失败url={}|原因:{}|message:{}",url, e.getMessage(), JSONUtil.toJsonStr(errorExternMsg));
        }
        return result;
    }
}
