package com.bfkt.forum.utils;

import com.bfkt.forum.enums.ExceptionEnum;
import com.bfkt.forum.exception.BizException;

import java.util.Collection;

public class AssertUtil {
    public static void notNull(Object o, ExceptionEnum code, Object ... params){
        if(o==null){
            throw new BizException(code,params);
        }
    }

    public static void isNull(Object o, ExceptionEnum code, Object ... params){
        if(o!=null){
            throw new BizException(code,params);
        }
    }

    public static void notBlank(String str, ExceptionEnum code, Object ... params){
        if(str==null || str.trim().isEmpty()){
            throw new BizException(code,params);
        }
    }

    public static void notEmpty(Collection<?> menuPerms, ExceptionEnum code, Object ... params) {
        if(menuPerms == null || menuPerms.isEmpty()){
            throw new BizException(code,params);
        }
    }

    public static void isTrue(boolean condition , ExceptionEnum code, Object ... params){
        if(!condition){
            throw new BizException(code,params);
        }
    }
}
