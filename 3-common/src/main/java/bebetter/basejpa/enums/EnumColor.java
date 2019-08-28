package bebetter.basejpa.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumColor implements IDictEnum {
    blue("蓝色", ""),
    green("绿色", "success"),
    grey("灰色", "info"),
    orange("橘色", "warning"),
    red("红色", "danger"),
    ;
    String label;
    String elTagType;
}
