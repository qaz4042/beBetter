package bebetter.basejpa.util;

import bebetter.statics.util.V;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public class IPUtils {
    public static final String getIpAddress(HttpServletRequest request) throws IOException {

        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 百度ip地址信息
     * https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=117.21.22.131&resource_id=6006
     * {"status":"0","t":"","set_cache_time":"","data":[{"location":"江西省九江市 电信","titlecont":"IP地址查询","origip":"117.21.22.131","origipquery":"117.21.22.131","showlamp":"1","showLikeShare":1,"shareImage":1,"ExtendedLocation":"","OriginQuery":"117.21.22.131","tplt":"ip","resourceid":"6006","fetchkey":"117.21.22.131","appinfo":"","role_id":0,"disp_type":0}]}
     */
    public static BaiDuIpInfo getBaiduIpAddressInfo(String ip) {
        Console.log("get ip info==>{}", ip);
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + ip + "&resource_id=6006";
        String result = HttpUtil.get(url, 2000, "IPUtils.getBaiduIpAddressInfo");
        Console.log(result);
        if (V.noEmpty(result)) {
            try {
                return JSON.parseObject(result, BaiDuIpInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public interface IpInfo {
        //国家
        String getCountry();

        //省份
        String getProvince();

        //市
        String getCity();

        //县级
        String getDistrict();
    }
}

@Data
class Info {
    String location;
}

@Data
class BaiDuIpInfo implements IPUtils.IpInfo {
    List<Info> data;
    Integer status;


    @Override
    public String getCountry() {
        return isChina() ? "中国" : getLocation();
    }

    @Override
    public String getProvince() {
        if (isChina()) {
            String location = getLocation();
            if (location.contains("省")) {
                return location.substring(0, location.indexOf("省") + 1);
            }
        }
        return "";
    }

    @Override
    public String getCity() {
        if (isChina()) {
            String location = getLocation();
            if (location.contains("省") && location.contains("市")) {
                return location.substring(location.indexOf("省") + 1, location.indexOf("市") + 1);
            }
        }
        return "";
    }

    @Override
    public String getDistrict() {
        return null;
    }

    private boolean isChina() {
        try {
            return getLocation().contains("省") || getLocation().contains("市");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getLocation() {
        if (V.noEmpty(data)) {
            Info info = data.get(0);
            if (null != info) {
                return info.getLocation();
            }
        }
        return "";
    }
}
