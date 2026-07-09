package com.xx.platform.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xx.platform.common.Result;
import com.xx.platform.entity.SysUser;
import com.xx.platform.entity.WebApp;
import com.xx.platform.service.AppService;
import com.xx.platform.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Web应用管理控制器
 * 公开接口：应用列表查询、详情、点击记录
 * 管理员接口：新增、编辑、删除
 */
@RestController
@RequestMapping("/api/apps")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private AuthService authService;

    /**
     * 获取应用列表（分页/筛选/排序）
     * GET /api/apps?page=1&size=12&categoryId=1&keyword=xxx&sortField=clickCount&sortOrder=desc
     */
    @GetMapping
    public Result<Page<WebApp>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false) String sortOrder) {
        return Result.success(appService.getAppList(page, size, categoryId, keyword, sortField, sortOrder));
    }

    /**
     * 获取应用详情
     * GET /api/apps/{id}
     */
    @GetMapping("/{id}")
    public Result<WebApp> detail(@PathVariable Integer id) {
        return Result.success(appService.getAppById(id));
    }

    /**
     * 新增应用（管理员）
     * POST /api/apps
     */
    @PostMapping
    public Result<Void> add(@RequestBody WebApp app,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        appService.addApp(app);
        return Result.success();
    }

    /**
     * 编辑应用（管理员）
     * PUT /api/apps/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody WebApp app,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        app.setId(id);
        appService.updateApp(app);
        return Result.success();
    }

    /**
     * 删除应用（管理员）
     * DELETE /api/apps/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        appService.deleteApp(id);
        return Result.success();
    }

    /**
     * 记录点击（公开）
     * POST /api/apps/{id}/click
     */
    @PostMapping("/{id}/click")
    public Result<Void> click(@PathVariable Integer id) {
        appService.incrementClickCount(id);
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
