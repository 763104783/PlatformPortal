package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.platform.entity.SysUser;
import com.xx.platform.mapper.SysUserMapper;
import com.xx.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户管理服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserMapper userMapper;

    @Override
    public Page<SysUser> getUserList(int page, int size) {
        Page<SysUser> pageParam = new Page<>(page, size);
        return userMapper.selectPage(pageParam,
                new LambdaQueryWrapper<SysUser>().orderByDesc(SysUser::getCreateTime));
    }

    @Override
    public void addUser(SysUser user) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, user.getUsername()));
        if (count > 0) {
            throw new RuntimeException("用户名已存在");
        }
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);
    }

    @Override
    public void updateUser(SysUser user) {
        user.setUpdateTime(new Date());
        userMapper.updateById(user);
    }

    @Override
    public void deleteUser(Integer id) {
        userMapper.deleteById(id);
    }
}
