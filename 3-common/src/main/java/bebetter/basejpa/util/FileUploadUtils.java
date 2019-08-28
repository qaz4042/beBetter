package bebetter.basejpa.util;

import bebetter.basejpa.enums.IEnumUploadModule;
import bebetter.statics.constant.C;
import bebetter.statics.model.KnowException;
import bebetter.basejpa.model.vo.UploadVo;
import bebetter.statics.util.V;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ImageUtil;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileUploadUtils {

    public static List<String> uploadFiles(MultipartFile[] files, String module) {
        bebetter.basejpa.enums.IEnumUploadModule moduleEnum = bebetter.basejpa.enums.IEnumUploadModule.codeObjMap.get(module);
        if (V.empty(files)) {
            throw new KnowException("上传失败|文件不能为空");
        }
        if (null == moduleEnum) {
            throw new KnowException("上传失败|模块不存在,请先定义(实现IEnumUploadModule)");
        }
        return Arrays.stream(files).map(file -> uploadFile(new bebetter.basejpa.model.vo.UploadVo(file), moduleEnum)).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    @SneakyThrows
    public static String uploadFile(UploadVo uploadFile, IEnumUploadModule moduleEnum) {
        String oriName = uploadFile.getOriName();

        //相对路径 (包括文件名,相对于根路径)
        String relativePath = moduleEnum.getPath() + C.x_斜杠 + IdUtil.simpleUUID() + C.x_下划线 + oriName;
        File saveFile = new File(EnvironmentUtil.BasePath + relativePath);
        if (!saveFile.getParentFile().exists()) {
            boolean mkdir = saveFile.getParentFile().mkdirs();
        }
        boolean newFile = saveFile.createNewFile();
        // 默认不压缩
        //如果是图片,并且要压缩,并且图片过大
        if (uploadFile.getIsImg() && moduleEnum.getImgCompress() && uploadFile.getSize() > moduleEnum.getImgCompressSize()) {
            float rate = uploadFile.getSize() / ((float) moduleEnum.getImgCompressSize());
            //统一转jpeg格式,压缩到1兆(若需要)
            @Cleanup FileOutputStream outputStream = new FileOutputStream(saveFile);
            @Cleanup InputStream inputStream1 = uploadFile.getInputStream();
            ImageUtil.scale(inputStream1, outputStream, rate);

        } else {
            uploadFile.transferTo(saveFile);
        }

        uploadFile.inputStreamClose();

        // todo  文件转存公共文件服务器
//        boolean ok = uploadToBase(saveFile);
//        if (!ok) {
//            throw new KnowException("文件上传公共文件服务器失败|请检查Param表upload相关配置");
//        }
        return relativePath.replaceAll("\\\\", "/");
    }

//    /**
//     * 文件转存公共文件服务器
//     */
//    @SneakyThrows
//    private static boolean uploadToBase(File saveFile) {
//        String host = EnumParamBase.upload_host.getValueCache();
//        Integer port = new Integer( V.or(EnumParamBase.upload_port.getValueCache(),"21"));
//        String username = V.or(EnumParamBase.upload_username.getValueCache(), "anonymous");
//        String password = V.or(EnumParamBase.upload_password.getValueCache(), "");
//        Ftp ftp = new Ftp(host, port, username, password);
//        String parent = saveFile.getParent();
//        ftp.mkdir(parent);
//        boolean upload = ftp.upload(parent, saveFile);
//        ftp.close();
//        return upload;
//    }

    /**
     * 判断是否是图片
     */
    public static boolean isImg(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).contains("image");
    }

    public static List<String> imgExt = Arrays.asList("jpg", "bmp", "jpeg", "png", "gif");

    /**
     * 判断是否是图片
     */
    public static boolean isImg(String name) {
        String extension = "";
        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i + 1);
        }
        return imgExt.contains(extension.toLowerCase());
    }

    public static File getFile(String relativePath) {
        return new File(EnvironmentUtil.BasePath + relativePath);
    }
}
