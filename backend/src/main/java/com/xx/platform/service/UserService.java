package com.xx.platform.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.platform.entity.SysUser;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 分页查询用户列表
     */
    Page<SysUser> getUserList(int page, int size);

    /**
     * 新增用户
     */
    void addUser(SysUser user);

    /**
     * 更新用户
     */
    void updateUser(SysUser user);

    /**
     * 删除用户
     */
    void deleteUser(Integer id);
}
