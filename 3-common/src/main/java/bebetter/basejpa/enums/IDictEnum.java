package bebetter.basejpa.enums;

import bebetter.basejpa.cfg.BaseMain;
import bebetter.basejpa.util.DictEnumUtil;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典接口
 * 可用来界面展示
 */
public interface IDictEnum {
    //字典key==code==name
    String name();

    default String getCode() {
        return name();
    }

    //字典value(名称)
    String getLabel();

    class Constant {
        public static Map<String, Class<? extends Enum>> simpleNameClassMap = BaseMain.getSubTypesOf(Enum.class)
                .stream().collect(Collectors.toMap(Class::getSimpleName, c -> c));

        public static Map<String, List<Map<String, ?>>> DictMapAll = simpleNameClassMap.keySet().stream().collect(Collectors.toMap(o -> o,
                className -> DictEnumUtil.getFullList(simpleNameClassMap.get(className))));
    }

    //    @JsonIgnore
//    @JSONField(serialize = false)
    default Serializable getValue() {
        return getCode();
    }
}
