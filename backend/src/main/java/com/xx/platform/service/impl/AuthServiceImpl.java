package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.SysUser;
import com.xx.platform.mapper.SysUserMapper;
import com.xx.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证服务实现类
 * 使用简单的内存Token机制（适合内部系统）
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private SysUserMapper userMapper;

    /** Token -> UserId 映射（生产环境建议用Redis） */
    private static final Map<String, Integer> TOKEN_STORE = new ConcurrentHashMap<>();

    @Override
    public Map<String, Object> login(String username, String password) {
        // 查询用户
        SysUser user = userMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getPassword, password)
        );
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成token
        String token = UUID.randomUUID().toString().replace("-", "");
        TOKEN_STORE.put(token, user.getId());

        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("role", user.getRole());
        return result;
    }

    @Override
    public SysUser getUserByToken(String token) {
        Integer userId = TOKEN_STORE.get(token);
        if (userId == null) {
            return null;
        }
        return userMapper.selectById(userId);
    }
}
