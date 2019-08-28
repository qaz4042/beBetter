package bebetter.basejpa.enums;

import bebetter.basejpa.cfg.BaseMain;
import bebetter.statics.constant.C;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 上传的模块
 */
public interface IEnumUploadModule extends IDictEnum {

    /**
     * 当文件是图片时,是否压缩
     */
    default Boolean getImgCompress() {
        return true;//默认要压缩
    }

    /**
     * 当文件是图片时,压缩成多大(比该值小则不压缩)
     * 单位Byte(1KB=1000Byte)
     */
    default Long getImgCompressSize() {
        return C._1Mb;//默认1M
    }

    /**
     * 文件存放目录,相对路径, 一般 = 分号+ 模块code(也是枚举的name)
     */
    default String getPath() {
        return C.x_斜杠 + getCode();
    }

    // 所有枚举
    Map<String, IEnumUploadModule> codeObjMap = BaseMain.getImplEnumsByIntf(IEnumUploadModule.class)
            .stream().collect(Collectors.toMap(IEnumUploadModule::getCode, o -> o));
}
