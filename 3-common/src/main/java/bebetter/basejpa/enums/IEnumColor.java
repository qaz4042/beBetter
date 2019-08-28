package bebetter.basejpa.enums;

public interface IEnumColor {
    String name();

    default String getCode() {
        return name();
    }

    String getLabel();

    String getStyle();
}
