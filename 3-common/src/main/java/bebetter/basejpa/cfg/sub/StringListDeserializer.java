package bebetter.basejpa.cfg.sub;/*
package bebetter.base.cfg.sub;


import bebetter.basejpa.bebetter.jpademo.model.math.StringList;
import bebetter.basejpa.utils.V;
import base.fasterxml.jackson.core.JsonParser;
import base.fasterxml.jackson.core.JsonProcessingException;
import base.fasterxml.jackson.databind.DeserializationContext;
import base.fasterxml.jackson.databind.JsonDeserializer;
import base.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.utils.Iterator;
import java.utils.List;
import java.utils.stream.Collectors;
import java.utils.stream.Stream;

*/
/**
 * jackson定制
 *//*

public class StringListDeserializer extends JsonDeserializer<StringList> {
    @Override
    public StringList deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
//            List<String> list = new ArrayList<>();

        String values = node.get("values").textValue();
        String valuesAppend = node.get("valuesAppend").textValue();
        String split = node.get("split").textValue();
        Iterator<JsonNode> list = node.get("list").elements();
        if (null != list) {
            List<String> strList = Stream.generate(list::next).map(JsonNode::textValue).collect(Collectors.toList());
            return StringList.of(strList);
        }
        return StringList.of(V.or(values, valuesAppend));
    }
}*/
