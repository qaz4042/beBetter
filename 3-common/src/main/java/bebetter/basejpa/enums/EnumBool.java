package bebetter.basejpa.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumBool implements IEnumColor {
    是("true", "green lighten-2 font-weight-medium"),//绿色 浅色 字体加粗
    否("false", "red lighten-2 font-weight-medium"),
    ;
    String code;
    String style;

    @Override
    public String getLabel() {
        return name();
    }

    public String getCode() {
        return this.code;
    }
}
