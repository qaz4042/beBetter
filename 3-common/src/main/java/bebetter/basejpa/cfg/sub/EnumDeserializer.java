/*
package bebetter.basejpa.cfg.sub;

import cn.hutool.core.util.EnumUtil;
import bebetter.statics.util.V;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnumDeserializer extends JsonDeserializer<Enum> implements ContextualDeserializer {
    private Class clz;

    @Override
    public Enum deserialize(JsonParser jsonParser, DeserializationContext ctx) throws IOException {
//        jsonParser.isExpectedStartObjectToken()
        if (V.empty(jsonParser.getText())) {
            return null;
        }
        return EnumUtil.fromString(clz, jsonParser.getText());
    }

    */
/**
     * 获取合适的解析器，把当前解析的属性Class对象存起来，以便反序列化的转换类型，为了避免线程安全问题，每次都new一个（通过threadLocal来存储更合理）
     *//*

    @Override
    public JsonDeserializer createContextual(DeserializationContext ctx, BeanProperty property) throws JsonMappingException {
        Class rawCls = ctx.getContextualType().getRawClass();
        EnumDeserializer clone = new EnumDeserializer();
        clone.setClz(rawCls);
        return clone;
    }
}
*/
