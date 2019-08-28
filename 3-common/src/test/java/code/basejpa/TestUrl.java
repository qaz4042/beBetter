package bebetter.basejpa;

import cn.hutool.core.io.FileUtil;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestUrl {
    public static final Pattern reg = Pattern.compile("url\\((.*?)\\) format\\(");

//    @Test
    public void test1() {
        String content = FileUtil.readUtf8String("C:/css.css");
        Matcher matcher = reg.matcher(content);
        String s = "";
        while (matcher.find()) {
            s += matcher.group(1) + "\n";
        }
        System.out.println(s);
    }

//    @Test
    public void test12() {
    }
}
