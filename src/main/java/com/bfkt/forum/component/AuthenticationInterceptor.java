package com.bfkt.forum.component;

import com.bfkt.forum.common.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class AuthenticationInterceptor implements HandlerInterceptor {
    Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return true;
        }
        Cookie tokenCookie = null;
        for (Cookie cookie : cookies) {
            if (Constants.TOKEN_COOKIE_NAME.equals(cookie.getName())) {
                tokenCookie = cookie;
                break;
            }
        }
        if (tokenCookie != null) {
            AuthUserContext.authUser(tokenCookie.getValue(), response);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AuthUserContext.clear();
    }
}
