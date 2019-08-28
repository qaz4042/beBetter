package bebetter.basejpa.cfg;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.SneakyThrows;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class Converter_jsonArray implements AttributeConverter<JSONArray, String> {

//    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    @SneakyThrows
    public String convertToDatabaseColumn(JSONArray data) {
        if (null == data) {
            return null;
        }
        return data.toString();
    }

    @Override
    @SneakyThrows
    public JSONArray convertToEntityAttribute(String dbData) {
        if (null == dbData) {
            return null;
        }
//        Class<?> typeArgument = ClassUtil.getTypeArgument(this.getClass(), 1);
//        System.out.println("typeArgument========" + typeArgument);
        return new JSONArray(dbData);
    }

}
