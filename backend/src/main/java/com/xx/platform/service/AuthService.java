package com.xx.platform.service;

import com.xx.platform.entity.SysUser;

import java.util.Map;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 包含token和用户信息的Map
     */
    Map<String, Object> login(String username, String password);

    /**
     * 根据token获取用户信息
     * @param token 登录token
     * @return 用户信息（不含密码）
     */
    SysUser getUserByToken(String token);
}
