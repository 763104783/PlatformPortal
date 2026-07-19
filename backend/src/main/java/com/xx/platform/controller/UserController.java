package com.xx.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.platform.common.Result;
import com.xx.platform.entity.SysUser;
import com.xx.platform.service.AuthService;
import com.xx.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户管理控制器
 * 管理员专用：用户的增删改查
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    /**
     * 获取用户列表（分页）
     * GET /api/user/page?page=1&size=10
     */
    @GetMapping("/page")
    public Result<Page<SysUser>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        return Result.success(userService.getUserList(page, size));
    }

    /**
     * 新增用户
     * POST /api/user
     */
    @PostMapping
    public Result<Void> add(@RequestBody SysUser user,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        userService.addUser(user);
        return Result.success();
    }

    /**
     * 编辑用户
     * PUT /api/user/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody SysUser user,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        user.setId(id);
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 删除用户
     * DELETE /api/user/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 批量删除用户
     * DELETE /api/user/batch
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        List<Integer> ids = params.get("ids");
        if (ids != null) {
            for (Integer id : ids) {
                userService.deleteUser(id);
            }
        }
        return Result.success();
    }

    /**
     * 校验管理员权限
     */
    private void checkAdmin(String token) {
        if (token == null) {
            throw new RuntimeException("请先登录");
        }
        SysUser user = authService.getUserByToken(token);
        if (user == null || !"ADMIN".equals(user.getRole())) {
            throw new RuntimeException("无管理员权限");
        }
    }
}
