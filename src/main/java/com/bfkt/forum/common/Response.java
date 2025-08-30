package com.bfkt.forum.common;

import cn.hutool.core.util.StrUtil;
import com.bfkt.forum.enums.ExceptionEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Response<T> {
    private String code;
    private String msg;
    private T data;

    public Response(String code, String msg, T data) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>("0", "ok", data);
    }

    public static <T> Response<T> fail(String code, String msg) {
        return new Response<>(code, msg, null);
    }

    public static <T> Response<T> fail(ExceptionEnum code, Object... params) {
        String msg;
        if (params.length != 0) {
            msg = StrUtil.format(code.getDesc(), params);
        } else {
            msg = code.getDesc();
        }
        return new Response<>(code.getCode(), msg, null);
    }

    @JsonIgnore
    public boolean isOk() {
        return code.equals("0") || code.equals("200");
    }
}
