package com.bfkt.forum.component;

import com.bfkt.forum.utils.CommonUtil;
import org.springframework.format.Formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {

    @Override
    public LocalDateTime parse(String s, Locale locale) {
        return CommonUtil.convertLocalDateTime(s);
    }

    @Override
    public String print(LocalDateTime localDateTime, Locale locale) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
