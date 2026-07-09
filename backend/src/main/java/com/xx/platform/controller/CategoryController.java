package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.entity.AppCategory;
import com.xx.platform.entity.SysUser;
import com.xx.platform.service.AuthService;
import com.xx.platform.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用分类控制器
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AuthService authService;

    /**
     * 获取所有分类列表
     * GET /api/categories
     */
    @GetMapping
    public Result<List<AppCategory>> list() {
        return Result.success(categoryService.getAllCategories());
    }

    /**
     * 新增分类（管理员）
     * POST /api/categories
     */
    @PostMapping
    public Result<Void> add(@RequestBody AppCategory category,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        categoryService.addCategory(category);
        return Result.success();
    }

    /**
     * 编辑分类（管理员）
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, @RequestBody AppCategory category,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        category.setId(id);
        categoryService.updateCategory(category);
        return Result.success();
    }

    /**
     * 删除分类（管理员）
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        categoryService.deleteCategory(id);
        return Result.success();
    }

    private void checkAdmin(String token) {
        if (token == null) throw new RuntimeException("请先登录");
        SysUser user = authService.getUserByToken(token);
        if (user == null || !"ADMIN".equals(user.getRole())) throw new RuntimeException("无管理员权限");
    }
}
