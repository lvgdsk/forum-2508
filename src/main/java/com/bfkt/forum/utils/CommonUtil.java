package com.bfkt.forum.utils;

import com.bfkt.forum.common.Constants;
import com.bfkt.forum.enums.ExceptionEnum;
import com.bfkt.forum.exception.BizException;
import jakarta.servlet.http.Cookie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.util.Date;

import static java.time.temporal.ChronoField.*;

public class CommonUtil {
    private static final int COOKIE_ONE_HOUR = 18000;


    public static final DateTimeFormatter DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .appendValue(HOUR_OF_DAY, 2)
            .appendLiteral(':')
            .appendValue(MINUTE_OF_HOUR, 2)
            .appendLiteral(':')
            .appendValue(SECOND_OF_MINUTE, 2)
            .toFormatter();

    private static final DateTimeFormatter BAR_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .appendLiteral(' ')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .optionalStart()
            .appendOffsetId()
            .optionalStart()
            .appendLiteral('[')
            .parseCaseSensitive()
            .appendZoneRegionId()
            .appendLiteral(']')
            .toFormatter();

    private static final DateTimeFormatter SLASH_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendValue(YEAR, 4, 10, SignStyle.EXCEEDS_PAD)
            .appendLiteral('/')
            .appendValue(MONTH_OF_YEAR, 2)
            .appendLiteral('/')
            .appendValue(DAY_OF_MONTH, 2)
            .appendLiteral(' ')
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .optionalStart()
            .appendOffsetId()
            .optionalStart()
            .appendLiteral('[')
            .parseCaseSensitive()
            .appendZoneRegionId()
            .appendLiteral(']')
            .toFormatter();

    public static Cookie buildCookie(String token) {
        return buildCookie(Constants.TOKEN_COOKIE_NAME, token, COOKIE_ONE_HOUR * 5);
    }

    public static Cookie buildCookie(String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

    /**
     * 生成用户token
     *
     * @param userId     用户id
     * @return 用户token
     */
    public static String generateUserToken(Long userId) {
        String token = "SA";
        if (userId == null) {
            userId = 0L;
        }
        String idStr = SfsUtil.to64Str(userId + 10000);
        token += idStr;
        token += SfsUtil.randomStr(Constants.TOKEN_LENGTH - "SA".length() - idStr.length());
        return token;
    }

    public static LocalDateTime convertLocalDateTime(String source) {
        String target = source;
        if (target == null || target.trim().isEmpty()) {
            return null;
        }
        target = target.trim();
        try {
            if (target.length() == 10) {
                target += " 00:00:00";
            }
            if (target.charAt(4) == '-') {
                if (target.charAt(10) == 'T') {
                    // 能解析：2023-03-01T09:00:00、2023-03-01T09:00:00:000、2023-03-01T09:00:00:000Z
                    return LocalDateTime.parse(target, DateTimeFormatter.ISO_DATE_TIME);
                } else {
                    // 能解析：2023-03-01 09:00:00、2023-03-01 09:00:00:000、2023-03-01 09:00:00:000Z
                    return LocalDateTime.parse(target, BAR_DATE_FORMATTER);
                }
            } else if (target.charAt(4) == '/') {
                // 能解析：2023/03/01 09:00:00、2023/03/01 09:00:00:000、2023/03/01 09:00:00:000Z
                return LocalDateTime.parse(target, SLASH_DATE_FORMATTER);
            }
            return LocalDateTime.ofInstant(new Date(Long.parseLong(target)).toInstant(),
                    java.time.ZoneId.systemDefault());
        } catch (Exception e) {
            throw new BizException(ExceptionEnum.ERROR_CODE_0025, source);
        }
    }

}
