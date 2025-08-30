package com.bfkt.forum.component;


import com.bfkt.forum.utils.CommonUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.time.LocalDateTime;

public class MyLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> implements Converter<String, LocalDateTime> {
    public LocalDateTime convert(String source) {
        return CommonUtil.convertLocalDateTime(source);
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return CommonUtil.convertLocalDateTime(jsonParser.getText());
    }
}
