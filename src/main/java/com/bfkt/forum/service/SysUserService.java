package com.bfkt.forum.service;

import cn.hutool.cache.impl.TimedCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bfkt.forum.common.Constants;
import com.bfkt.forum.component.AuthUserContext;
import com.bfkt.forum.component.Encryptor;
import com.bfkt.forum.entity.SysUser;
import com.bfkt.forum.enums.ExceptionEnum;
import com.bfkt.forum.utils.AssertUtil;
import com.bfkt.forum.utils.CommonUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SysUserService {

    private final TimedCache<String, SysUser> loginUserMap = new TimedCache<>(1000 * 60 * 30);
    private final ISysUserService iSysUserService;

    public void getByToken(String token, HttpServletResponse response) {
        SysUser user = loginUserMap.get(token, true);
        AuthUserContext.setAuthUser(user);
    }

    public void clearToken(HttpServletResponse response) {
        Cookie cookie;
        cookie = new Cookie(Constants.TOKEN_COOKIE_NAME, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        loginUserMap.remove(AuthUserContext.getToken());
    }

    public SysUser login(SysUser sysUser, HttpServletResponse response) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(SysUser.class);
        queryWrapper.eq(SysUser::getUsername, sysUser.getUsername());
        SysUser user = iSysUserService.getOne(queryWrapper);
        AssertUtil.notNull(user, ExceptionEnum.ERROR_CODE_0002);
        AssertUtil.isTrue(user.getPasswd().equals(Encryptor.bcryptEncrypt(sysUser.getPasswd())), ExceptionEnum.ERROR_CODE_0002);
        String token = CommonUtil.generateUserToken(user.getId());
        loginUserMap.put(token, user);
        AuthUserContext.setToken(token);
        AuthUserContext.setAuthUser(user);
        Cookie cookie = CommonUtil.buildCookie(token);
        response.addCookie(cookie);
        return user;
    }


    public void register(SysUser sysUser, HttpServletResponse response) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery(SysUser.class);
        queryWrapper.eq(SysUser::getUsername, sysUser.getUsername());
        AssertUtil.isTrue(iSysUserService.count(queryWrapper) == 0, ExceptionEnum.ERROR_CODE_0001);
        String originPasswd = sysUser.getPasswd();
        sysUser.setPasswd(Encryptor.bcryptEncrypt(sysUser.getPasswd()));
        iSysUserService.save(sysUser);
        sysUser.setPasswd(originPasswd);
        login(sysUser, response);
    }

    public void logout(HttpServletResponse response) {
        clearToken(response);
    }
}
