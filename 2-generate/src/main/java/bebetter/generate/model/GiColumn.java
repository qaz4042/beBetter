package bebetter.generate.model;

import bebetter.generate.annotation.GColumn;
import bebetter.generate.enums.EnumInputType;
import bebetter.statics.util.V;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Builder
@Data
public class GiColumn {
    public String name;//属性名

    //注解提取
    public String label;//中文名 value
    public EnumInputType inputType;//界面类型
    public String defValue;//默认值 mysql自动转类型
    public String classname;
    public Class clazz;
    public String ruleArray;//校验 必填/邮箱/手机/数字/最大/最小/最大长度/最小长度/
    public Boolean sortable;//
    public Boolean isEnum;//
    public Boolean hideUser;//用户端隐藏

    public static final Pattern DefaultValueReg = Pattern.compile("default (.*?)");

    static GiColumn fromFiled(Field filed) {
        Class type = filed.getType();
        boolean sortable = Number.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type) || Temporal.class.isAssignableFrom(type);
        GiColumnBuilder builder = GiColumn.builder()
                .name(filed.getName())
                .label(filed.getName())
                .inputType(EnumInputType.fromField(filed))
                .classname(type.getSimpleName())
                .clazz(type)
                .sortable(sortable)
                .isEnum(type.isEnum());
        GColumn gColumn = filed.getAnnotation(GColumn.class);
        List<String> rules = new ArrayList<>(8);
        if (null != gColumn) {
            builder.label(gColumn.value());
            rules.addAll(Arrays.asList(gColumn.rules()));
        }
        Column column = filed.getAnnotation(Column.class);
        if (null != column) {
            String definition = column.columnDefinition();
            if (V.noEmpty(definition)) {
                Matcher matcher = DefaultValueReg.matcher(definition);
                if (matcher.matches()) {
                    builder.defValue(matcher.group(0).replace("default ", "").trim());//字段默认值
                }
            }
        }
        builder.ruleArray("[" + rules.stream().filter(V::noEmpty).collect(Collectors.joining(",")) + "]");
        return builder.build();
    }

/*    private static Map<String, String> extMap(String s) {
        if (null == s) {
            return new HashMap<>(0);
        }
        return Arrays.stream(s.split(",")).filter(V::noEmpty).collect(Collectors.toMap(sub -> sub.split(":")[0], sub -> sub.split(":")[1]));
    }*/
}
