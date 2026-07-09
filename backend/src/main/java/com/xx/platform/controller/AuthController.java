package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.entity.SysUser;
import com.xx.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 * 处理登录、登出、获取当前用户信息
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 用户登录
     * POST /api/auth/login
     * @param params 包含 username 和 password
     * @return token和用户信息
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        if (username == null || password == null) {
            return Result.error("用户名和密码不能为空");
        }
        Map<String, Object> result = authService.login(username, password);
        return Result.success(result);
    }

    /**
     * 用户登出
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        // 简单实现：客户端清除token即可，服务端token自动失效
        return Result.success();
    }

    /**
     * 获取当前登录用户信息
     * GET /api/auth/info
     * @param token 请求头中的token
     * @return 用户信息（不含密码）
     */
    @GetMapping("/info")
    public Result<SysUser> getUserInfo(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null) {
            return Result.error(401, "未登录");
        }
        SysUser user = authService.getUserByToken(token);
        if (user == null) {
            return Result.error(401, "登录已过期");
        }
        user.setPassword(null); // 不返回密码
        return Result.success(user);
    }
}
