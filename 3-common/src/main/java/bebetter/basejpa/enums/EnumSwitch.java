package bebetter.basejpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 开关  的枚举
 */
@AllArgsConstructor
@Getter
public enum EnumSwitch implements IDictEnum {
    开启,
    关闭,
    ;

    @Override
    public String getLabel() {
        return name();
    }
}
