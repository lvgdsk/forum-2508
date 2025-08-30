package com.bfkt.forum.component;

import com.bfkt.forum.common.Constants;
import com.bfkt.forum.entity.SysUser;
import com.bfkt.forum.enums.ExceptionEnum;
import com.bfkt.forum.service.SysUserService;
import com.bfkt.forum.utils.AssertUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthUserContext {
    private static final ThreadLocal<SysUser> currentAuthUser = new InheritableThreadLocal<>();
    private static final ThreadLocal<String> token = new InheritableThreadLocal<>();
    private static SysUserService sysUserService;

    public AuthUserContext(SysUserService sysUserService) {
        AuthUserContext.sysUserService = sysUserService;
    }

    public static void authUser(String token, HttpServletResponse response) {
        if (token == null || token.length() != Constants.TOKEN_LENGTH) {
            sysUserService.clearToken(response);
        } else {
            sysUserService.getByToken(token, response);
        }
    }

    public static SysUser getUser() {
        return currentAuthUser.get();
    }

    public static void setAuthUser(SysUser sysUser) {
        currentAuthUser.set(sysUser);
    }

    public static void clear() {
        currentAuthUser.remove();
        token.remove();
    }

    public static void needLogin() {
        AssertUtil.notNull(currentAuthUser.get(), ExceptionEnum.NOT_AUTH);
    }

    public static String getToken() {
        return token.get();
    }

    public static void setToken(String token) {
        AuthUserContext.token.set(token);
    }
}
