/*
package bebetter.basejpa.cfg;

import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class Converter_jsonObject implements AttributeConverter<JSONObject, String> {

//    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(JSONObject data) {
        if (null == data) {
            return null;
        }
        return data.toString();
    }

    @Override
    @SneakyThrows
    public JSONObject convertToEntityAttribute(String dbData) {
        if (null == dbData) {
            return null;
        }
//        Class<?> typeArgument = ClassUtil.getTypeArgument(this.getClass(), 1);
//        System.out.println("typeArgument========" + typeArgument);
        return new JSONObject(dbData);
    }

}
*/
