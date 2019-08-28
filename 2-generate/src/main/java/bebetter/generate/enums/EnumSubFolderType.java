package bebetter.generate.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件目录划分
 */
@AllArgsConstructor
@Getter
public enum EnumSubFolderType {
    统一在gengrate目录下,
    以entityName分目录,
    以文件类型EnumFile分目录,
    ;
}
