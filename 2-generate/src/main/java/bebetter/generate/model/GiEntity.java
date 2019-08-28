package bebetter.generate.model;

import bebetter.generate.annotation.GEntity;
import bebetter.generate.enums.EnumFileType;
import bebetter.statics.constant.C;
import bebetter.statics.model.IUserId;
import bebetter.statics.model.KnowException;
import bebetter.statics.util.ClassUtil;
import bebetter.statics.util.FileUtils;
import bebetter.statics.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@Slf4j
public class GiEntity {
    static final String Split = File.separator;

    String label;               //中文名
    String name;                //英文名,
    String nameUp;//属性名头字母大写

    Class clazz;                //类
    List<GiColumn> columns;
    String dictClasses;

    String packageEntity;
    String packageDao;
    String packageService;
    String packageController;
    String packageAdminController;
    String packageDto;
    String classnameEntity;
    String classnameDao;
    String classnameService;
    String classnameController;
    String classnameDto;

    Set<String> importClasses;

    String isSpecial;// 如果model实现Principle接口(比如User),若放controller方法入参中,系统会自动读取当前登录用户信息放上去,若未登录即为null

    public String baseController;// BaseController全查询  或者 BaseUserController当前登录用户数据的增删改查

//    public Boolean userReadOnly;//对user是否只读


    public static GiEntity fromClass(Class modelClass) {
        GEntity gClass = (GEntity) modelClass.getAnnotation(GEntity.class);
        String nameUp = modelClass.getSimpleName();
        String name = StrUtil.lowerFirst(nameUp);
        List<GiColumn> columns = ClassUtil.fields(modelClass).stream()
                .map(GiColumn::fromFiled)
                .collect(Collectors.toList());
        return GiEntity.builder()
                .label(gClass.value())
                .name(name)
                .nameUp(nameUp)
                .clazz(modelClass)
                .columns(columns)
                .dictClasses(columns.stream().filter(GiColumn::getIsEnum).map(c -> "\'" + c.getClassname() + "\',").collect(Collectors.joining("")))
                .importClasses(columns.stream().map(c -> c.getClazz().getName()).filter(classname -> !classname.startsWith("java.lang.")).collect(Collectors.toSet()))
                .packageEntity(modelClass.getPackage().getName())
                .packageDao(getPackage(name, EnumFileType.dao))
                .packageService(getPackage(name, EnumFileType.service))
                .packageController(getPackage(name, EnumFileType.controller))
                .packageAdminController(getPackage(name, EnumFileType.admin_controller))
                .packageDto(getPackage(name, EnumFileType.dto))
                .classnameEntity(nameUp)
                .classnameDao(EnumFileType.dao.getFileName(name, nameUp))
                .classnameService(EnumFileType.service.getFileName(name, nameUp))
                .classnameController(EnumFileType.controller.getFileName(name, nameUp))
                .classnameController(EnumFileType.dto.getFileName(name, nameUp))
                .baseController(IUserId.class.isAssignableFrom(modelClass) ? "BaseUserController" : "BaseController")
                .build();
    }

    //获取文件子目录
    private static String getSubPath(String entityName, EnumFileType fileType) {
        switch (GiCfg.生成文件二级的路径类型) {
            case 统一在gengrate目录下:
                return "";
            case 以entityName分目录:
                return File.separator + entityName;
            case 以文件类型EnumFile分目录:
                return File.separator + fileType.getSubFolder();
        }
        throw new KnowException("获取文件子目录异常");
    }

    //获取文件包名 model/dao/service/controller的包名
    private static String getPackage(String entityName, EnumFileType fileType) {
        String outPackage = GiCfg.生成文件的根路径对应包名;
        switch (GiCfg.生成文件二级的路径类型) {
            case 统一在gengrate目录下:
                return outPackage;
            case 以entityName分目录:
                return outPackage + "." + entityName;
            case 以文件类型EnumFile分目录:
                return outPackage + "." + fileType.getSubFolder().replaceAll("\\\\", ".").replaceAll("/", ".");
        }
        throw new KnowException("获取文件包名异常");
    }

    @SneakyThrows
    public void newFiles() {
        String entityName = this.name;
        VelocityContext ctx = new VelocityContext();
        Arrays.stream(this.getClass().getDeclaredFields()).forEach((field -> {
            try {
                ctx.put(field.getName(), field.get(this));
            } catch (Exception ignore) {

            }
        }));

        for (EnumFileType enumFileType : EnumFileType.values()) {
            String subPath = getSubPath(entityName, enumFileType) + Split + enumFileType.getFileName(name, nameUp);
            String filePath = GiCfg.get生成文件的根路径() + subPath;
            if (new File(filePath).exists()) {
                log.info("文件已存在|跳过:" + subPath);
            } else {
                Writer writer = new StringWriter();
                Template template = enumFileType.getTemplate(this);
                if (null != template) {
                    template.merge(ctx, writer);
                }
                FileUtils.writeCover(new File(filePath), writer.toString(), true, C.UTF_8);
                writer.close();
                log.info("生成了:" + subPath);
            }
        }
    }
}
