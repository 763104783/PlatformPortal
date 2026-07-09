package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.entity.ShowcaseItem;
import com.xx.platform.entity.SysUser;
import com.xx.platform.service.AuthService;
import com.xx.platform.service.ShowcaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宣贯数据控制器
 */
@RestController
@RequestMapping("/api/showcase")
public class ShowcaseController {

    @Autowired
    private ShowcaseService showcaseService;

    @Autowired
    private AuthService authService;

    /**
     * 按类别获取宣贯项列表
     * GET /api/showcase?category=USER_ECOLOGY
     */
    @GetMapping
    public Result<List<ShowcaseItem>> list(@RequestParam(required = false) String category) {
        return Result.success(showcaseService.getShowcaseItems(category));
    }

    /**
     * 获取宣贯项详情
     * GET /api/showcase/{id}
     */
    @GetMapping("/{id}")
    public Result<ShowcaseItem> detail(@PathVariable Integer id) {
        return Result.success(showcaseService.getShowcaseById(id));
    }

    /**
     * 新增宣贯项（管理员）
     * POST /api/showcase
     */
    @PostMapping
    public Result<Void> add(@RequestBody ShowcaseItem item,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        showcaseService.addShowcaseItem(item);
        return Result.success();
    }

    /**
     * 编辑宣贯项（管理员）
     * PUT /api/showcase/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody ShowcaseItem item,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        item.setId(id);
        showcaseService.updateShowcaseItem(item);
        return Result.success();
    }

    /**
     * 删除宣贯项（管理员）
     * DELETE /api/showcase/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        showcaseService.deleteShowcaseItem(id);
        return Result.success();
    }

    private void checkAdmin(String token) {
        if (token == null) throw new RuntimeException("请先登录");
        SysUser user = authService.getUserByToken(token);
        if (user == null || !"ADMIN".equals(user.getRole())) throw new RuntimeException("无管理员权限");
    }
}
