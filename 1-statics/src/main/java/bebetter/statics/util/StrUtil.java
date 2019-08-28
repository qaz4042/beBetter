package bebetter.statics.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * StringUtil
 */
public class StrUtil {
    /**
     * 是否包含中文
     */
    public static boolean isContainsChinese(String str) {
        if (null == str) {
            return false;
        }
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 截取前几个字符,若不够长直接返回
     */
    public static String getHead(String s, int maxLength) {
        if (null != s) {
            if (s.length() > maxLength) {
                return s.substring(0, maxLength);
            }
            return s;
        }
        return null;
    }

    /**
     * 替换中间字符
     */
    public static String replaceMiddle(String ori, String replace, int beginIndex, int endIndex) {
        int begin = Math.max(beginIndex, 0);
        int end = Math.min(ori.length(), endIndex);
        String xings = IntStream.range(0, end - begin).mapToObj(i -> "*").collect(Collectors.joining());
        return ori.substring(0, begin) + xings + ori.substring(end);
    }

    /**
     * 中间替换成*(除了前后4位)
     */
    public static String replaceXing(String ori) {
        return replaceMiddle(ori, "*", 4, 4);
    }

    /**
     * 不足长度时,前面拼0(零)
     */
    public static String append0ToHead(Serializable ori, int maxLength) {
        String str = ori.toString();
        int appendSize = maxLength - str.length();
        if (appendSize <= 0) {
            return str;
        }
        return IntStream.range(0, appendSize).mapToObj(i -> "0").collect(Collectors.joining()) + ori;
    }

    public static String appendParam(Map map, String split) {
        StringBuilder str = new StringBuilder();
        for (Object key : map.keySet()) {
            str.append(key.toString()).append("=").append(map.get(key)).append(split);
        }
        return str.substring(0, str.length() - split.length());
    }

    /**
     * 字符串转字符串List(每个字符都独立作为字符串)
     */
    public static List<String> strToStrList(String append) {
        List<String> list = new ArrayList<>();
        for (char c : append.toCharArray()) {
            list.add(new String(new char[]{c}));
        }
        return list;
    }

    /**
     * 小写首字母<br>
     */
    public static String lowerFirst(String str) {
        if (null == str) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isUpperCase(firstChar)) {
                return Character.toLowerCase(firstChar) + str.substring(1);
            }
        }
        return str;
    }

    public static String upperFirst(String str) {
        if (null == str) {
            return null;
        }
        if (str.length() > 0) {
            char firstChar = str.charAt(0);
            if (Character.isLowerCase(firstChar)) {
                return Character.toUpperCase(firstChar) + str.substring(1);
            }
        }
        return str;
    }


    public static String format(String text, Object... args) {
        return Parser.parse("{", "}", text, args);
    }
}
