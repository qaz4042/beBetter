package bebetter.generate;

import bebetter.generate.annotation.GEntity;
import bebetter.generate.enums.EnumSubFolderType;
import bebetter.generate.model.GenerateEntityInfo;
import bebetter.statics.util.ClassUtil;
import bebetter.statics.util.StrUtil;
import bebetter.statics.util.V;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@Slf4j
public class Generator {
    private static final String split = Matcher.quoteReplacement(File.separator);


    public static void generate(String scanPackage) {
        generate(scanPackage, scanPackage, null, (String[]) null);
    }

    /**
     * @param scanPackage       包名
     * @param subFolderType     这些文件的分录分隔
     * @param targetEntityNames 指定部分实体  不指定表示全部
     */
    public static void generate(@NotNull String scanPackage, @NotNull String outputPackageBase, EnumSubFolderType subFolderType, String... targetEntityNames) {
        String 生成文件的根路径对应包名 = ObjectUtil.defaultIfBlank(outputPackageBase, "generate");

        //按entityName_还是按文件类型(vue/dao/service),分文件夹
        EnumSubFolderType 生成文件二级的路径类型 = EnumSubFolderType.以文件类型EnumFile分目录;

        initVelocity();

        Set<GenerateEntityInfo> entitys = getEntitys(scanPackage, 生成文件的根路径对应包名, 生成文件二级的路径类型, targetEntityNames);

        log.info("\n---------------------开始生成---------------------\n-------根路径:{}-------\n-------entitys:{}-------", 生成文件的根路径对应包名, entitys.stream().map(GenerateEntityInfo::getName).collect(Collectors.joining(",")));

        entitys.forEach(e -> e.newFiles(get生成文件的根路径(生成文件的根路径对应包名), 生成文件二级的路径类型));
    }

    private static Set<GenerateEntityInfo> getEntitys(String pack, String 生成文件的根路径对应包名, EnumSubFolderType 生成文件二级的路径类型, String... entityNames) {
        if (null == pack) {
            return new HashSet<>(0);
        }
//        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(pack, GEntity.class);
        Set<Class<?>> classes = ClassUtil.getClasses(pack).stream().filter(c -> null != c.getAnnotation(GEntity.class)).collect(Collectors.toSet());
        if (V.noEmpty(entityNames)) {
            Set<String> set = Arrays.stream(entityNames).map(StrUtil::upperFirst).collect(Collectors.toSet());
            classes = classes.stream().filter(c -> set.contains(c.getSimpleName())).collect(Collectors.toSet());
        }
        return classes.stream().map(c -> GenerateEntityInfo.fromClass(c, 生成文件的根路径对应包名, 生成文件二级的路径类型)).collect(Collectors.toSet());
    }

    private static void initVelocity() {
        Properties ve = new Properties();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init(ve);
    }

    private static String get生成文件的根路径(String 生成文件的根路径对应包名) {
        String subPath = V.or(生成文件的根路径对应包名, "generate");
        return StrUtil.format("{}/src/main/java/{}", System.getProperty("user.dir"), subPath).replaceAll("/", split).replaceAll("\\.", split);
    }
}
