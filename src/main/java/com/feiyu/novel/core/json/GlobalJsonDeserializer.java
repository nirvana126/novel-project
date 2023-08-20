package com.feiyu.novel.core.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class GlobalJsonDeserializer {

    public static class StringDeserializer extends JsonDeserializer<String>{

        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {

            return jsonParser.getValueAsString()
                    .replace("<","&lt;")
                    .replace(">","&gt;");
        }
    }
}
