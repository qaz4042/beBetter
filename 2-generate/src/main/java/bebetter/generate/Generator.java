package bebetter.generate;

import bebetter.generate.annotation.GEntity;
import bebetter.generate.enums.EnumSubFolderType;
import bebetter.generate.model.GiCfg;
import bebetter.generate.model.GiEntity;
import bebetter.statics.util.ClassUtil;
import bebetter.statics.util.StrUtil;
import bebetter.statics.util.V;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Generator {

    public static void generate(String scanPackage) {
        generate(scanPackage, scanPackage, null, (String[]) null);
    }

    /**
     * @param scanPackage       包名
     * @param subFolderType     这些文件的分录分隔
     * @param targetEntityNames 指定部分实体  不指定表示全部
     */
    public static void generate(@NotNull String scanPackage, @NotNull String outputPackageBase, EnumSubFolderType subFolderType, String... targetEntityNames) {
        if (null != outputPackageBase) {
            GiCfg.生成文件的根路径对应包名 = outputPackageBase;
        }
        if (null != subFolderType) {
            GiCfg.生成文件二级的路径类型 = subFolderType;
        }

        initVelocity();

        Set<GiEntity> entitys = getEntitys(scanPackage, targetEntityNames);

        log.info("\n---------------------开始生成---------------------\n-------根路径:{}-------\n-------entitys:{}-------", GiCfg.生成文件的根路径对应包名, entitys.stream().map(GiEntity::getName));

        entitys.forEach(GiEntity::newFiles);
    }

    private static Set<GiEntity> getEntitys(String pack, String... entityNames) {
        if (null == pack) {
            return new HashSet<>(0);
        }
//        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(pack, GEntity.class);
        Set<Class<?>> classes = ClassUtil.getClasses(pack).stream().filter(c -> null != c.getAnnotation(GEntity.class)).collect(Collectors.toSet());
        if (V.noEmpty(entityNames)) {
            Set<String> set = Arrays.stream(entityNames).map(StrUtil::upperFirst).collect(Collectors.toSet());
            classes = classes.stream().filter(c -> set.contains(c.getSimpleName())).collect(Collectors.toSet());
        }
        return classes.stream().map(GiEntity::fromClass).collect(Collectors.toSet());
    }

    private static void initVelocity() {
        Properties ve = new Properties();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init(ve);
    }


}
