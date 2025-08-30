package com.bfkt.forum.exception;

import cn.hutool.core.util.StrUtil;
import com.bfkt.forum.enums.ExceptionEnum;

public class BizException extends RuntimeException {

    private final String code;
    private final String msg;

    public BizException(ExceptionEnum code, Object... params) {
        if (params.length != 0) {
            this.msg = StrUtil.format(code.getDesc(), params);
        } else {
            this.msg = code.getDesc();
        }
        this.code = code.getCode();
    }

    public BizException(String code, String msg, Object... params) {
        if (params.length != 0) {
            this.msg = StrUtil.format(msg, params);
        } else {
            this.msg = msg;
        }
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
