package bebetter.generate.model;

import bebetter.generate.enums.EnumSubFolderType;
import bebetter.statics.util.StrUtil;
import bebetter.statics.util.V;

import java.io.File;
import java.util.regex.Matcher;

public class GiCfg {

    private static final String split = Matcher.quoteReplacement(File.separator);
    public static String 生成文件的根路径对应包名 = null;

    public static String get生成文件的根路径() {
        String subPath = V.or(生成文件的根路径对应包名, "generate");
        return StrUtil.format("{}/src/main/java/{}", System.getProperty("user.dir"), subPath).replaceAll("/", split).replaceAll("\\.", split);
    }

    //按entityName_还是按文件类型(vue/dao/service),分文件夹
    public static EnumSubFolderType 生成文件二级的路径类型 = EnumSubFolderType.以文件类型EnumFile分目录;
}
