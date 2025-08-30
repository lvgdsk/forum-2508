package com.bfkt.forum.controller;

import com.bfkt.forum.common.Response;
import com.bfkt.forum.entity.SysUser;
import com.bfkt.forum.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lvqi
 * @since 2025-08-29
 */
@RestController
@RequestMapping("/sys-user")
@RequiredArgsConstructor
@Validated
public class SysUserController {
    private final SysUserService sysUserService;

    @Operation(summary = "注册")
    @PostMapping("/register")
    public Response<Void> register(@RequestBody @Valid SysUser sysUser, HttpServletResponse response) {
        sysUserService.register(sysUser, response);
        return Response.ok(null);
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Response<SysUser> login(@RequestBody @Valid SysUser sysUser, HttpServletResponse response) {
        return Response.ok(sysUserService.login(sysUser, response));
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Response<Void> logout(HttpServletResponse response) {
        sysUserService.logout(response);
        return Response.ok(null);
    }
}
