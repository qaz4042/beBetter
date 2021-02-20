package bebetter.statics.constant;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Contant常量
 */
public class C {

    public static final String SYS_PACKAGE = "bebetter";
    public static final String SYS_FILE_SAVEPATH = "bebetter.file.savePath";

    public static final Long _1Mb = 1024 * 1024L;

    public static final String k_空字符 = "";
    public static final String k_空格 = " ";
    public static final String d_逗号 = ",";
    public static final String x_斜杠 = File.separator;
    public static final String x_下划线 = "_";

    public static final String defaultStr = "default";
    public static String UTF_8_Str = "UTF-8";
    public static Charset UTF_8 = Charset.forName(UTF_8_Str);
}
