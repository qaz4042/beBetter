package bebetter.generate.enums;

import bebetter.generate.annotation.GColumn;
import bebetter.statics.model.KnowException;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.lang.reflect.Field;

/**
 * 输入框类型
 * <p>
 * 枚举字段 默认select
 * Long/Double/Bigdecimal字段 默认number
 * Boolean字段 默认checkbox
 * 其他 默认text
 */
public enum EnumInputType {
    text,
    number,
    textarea,
    select,
    checkbox,
    datetime,
    date,
    time,
    json,
    complextext,//  富文本   需要用GType标准出来    String  数据库推荐为text类型
    image,  //      图片     需要用GType标准出来    String  数据库推荐为varchar(256)类型
    none,   //      根据字段类型自动判断
    ;

    public static final Integer DefaultColumnSize = 255;

    public static EnumInputType fromField(Field field) {
        GColumn gtype = field.getAnnotation(GColumn.class);
        if (null != gtype) {
            if (!none.equals(gtype.type())) {
                return gtype.type();
            }
        }
        Class<?> declaringClass = field.getType();
        String classname = declaringClass.getName();
        switch (classname) {
            case "java.lang.Long":
            case "java.lang.Integer":
            case "java.lang.Float":
            case "java.lang.Double":
            case "java.math.BigDecimal":
                return number;
            case "java.lang.Boolean":
                return checkbox;
            case "java.util.Date":
            case "java.time.LocalDateTime":
            case "java.time.LocalDate":
                return date;
            case "java.util.LocalTime":
                return time;
            default:
                Type type = field.getAnnotation(Type.class);
                if (null != type) {
                    //noinspection SwitchStatementWithTooFewBranches
                    switch (type.type()) {
                        case "json":
                            return json;
                    }
                }
                if (Enum.class.isAssignableFrom(declaringClass)) {
                    return select;
                }
                if ("java.lang.String".equals(classname)) {
                    Column annotation = field.getAnnotation(Column.class);
                    if (null != annotation && annotation.length() != DefaultColumnSize && annotation.length() > 64) {
                        return textarea;
                    }
                    return text;
                }
        }
        throw new KnowException(field + "中存在未知的字段类型" + field.getDeclaringClass());
    }
}
