package com.bfkt.forum.enums;

import cn.hutool.core.util.StrUtil;

public enum ExceptionEnum {
    NOT_AUTH("401", "未认证"),
    FORBIDDEN("403", "权限不足"),
    ERROR_CODE_01("EX01", "{}"),
    ERROR_CODE_02("EX02", "{}"),
    ERROR_CODE_03("EX03", "{}"),
    ERROR_CODE_04("EX04", "{}"),
    ERROR_CODE_05("EX05", "未知异常 : {}"),
    ERROR_CODE_06("EX06", "{}"),
    ERROR_CODE_07("EX07", "需输入验证码进行登录"),
    ERROR_CODE_0001("EX0001", "用户名已存在"),
    ERROR_CODE_0002("EX0002", "用户名或密码错误"),
    ERROR_CODE_0003("EX0003", ""),
    ERROR_CODE_0004("EX0004", ""),
    ERROR_CODE_0005("EX0005", ""),
    ERROR_CODE_0006("EX0006", ""),
    ERROR_CODE_0007("EX0007", ""),
    ERROR_CODE_0008("EX0008", ""),
    ERROR_CODE_0009("EX0009", ""),
    ERROR_CODE_0010("EX0010", ""),
    ERROR_CODE_0011("EX0011", ""),
    ERROR_CODE_0012("EX0012", ""),
    ERROR_CODE_0013("EX0013", ""),
    ERROR_CODE_0014("EX0014", ""),
    ERROR_CODE_0015("EX0015", ""),
    ERROR_CODE_0016("EX0016", ""),
    ERROR_CODE_0017("EX0017", ""),
    ERROR_CODE_0018("EX0018", ""),
    ERROR_CODE_0019("EX0019", ""),
    ERROR_CODE_0020("EX0020", ""),
    ERROR_CODE_0021("EX0021", ""),
    ERROR_CODE_0022("EX0022", ""),
    ERROR_CODE_0023("EX0023", ""),
    ERROR_CODE_0024("EX0024", ""),
    ERROR_CODE_0025("EX0025", "日期解析失败：{}"),
    ERROR_CODE_0026("EX0026", ""),
    ERROR_CODE_0027("EX0027", ""),
    ERROR_CODE_0028("EX0028", ""),
    ERROR_CODE_0029("EX0029", ""),
    ERROR_CODE_0030("EX0030", ""),

    ;
    private final String code;
    private final String desc;

    ExceptionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc(Object... params) {
        if (params.length != 0) {
            return StrUtil.format(desc, params);
        } else {
            return desc;
        }
    }

    public String getMsg(Object... params) {
        if (params.length != 0) {
            return code + " : " + StrUtil.format(desc, params);
        } else {
            return code + " : " + desc;
        }
    }
}
