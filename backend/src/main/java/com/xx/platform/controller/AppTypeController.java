package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.entity.AppType;
import com.xx.platform.service.AppTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 应用类型控制器
 * 
 * 提供应用类型（枚举量）的REST API接口：
 * - GET  /api/app-type/list       获取所有应用类型列表（公开）
 * - POST /api/app-type            新增应用类型（管理员）
 * - PUT  /api/app-type/{id}       编辑应用类型（管理员）
 * - DELETE /api/app-type/{id}     删除应用类型（管理员）
 * - DELETE /api/app-type/batch    批量删除应用类型（管理员）
 * 
 * 权限说明：
 * - 查询接口：所有用户可访问
 * - 增删改接口：仅管理员可访问（通过Authorization头验证）
 */
@RestController
@RequestMapping("/api/app-type")
public class AppTypeController {

    @Autowired
    private AppTypeService appTypeService;

    /**
     * 获取所有应用类型列表
     * 
     * 接口说明：返回所有应用类型，按排序号升序排列
     * 权限要求：公开（无需登录）
     * 
     * @return 应用类型列表
     * 响应示例：
     * {
     *   "code": 200,
     *   "data": [
     *     { "id": 1, "name": "数据分析类", "description": "...", "sortOrder": 1 }
     *   ]
     * }
     */
    @GetMapping("/list")
    public Result<List<AppType>> list() {
        return Result.success(appTypeService.listAll());
    }

    /**
     * 新增应用类型
     * 
     * 接口说明：创建一个新的应用类型
     * 权限要求：管理员
     * 
     * @param appType 应用类型信息
     * 请求体示例：
     * { "name": "数据分析类", "description": "面向数据分析场景", "sortOrder": 1 }
     */
    @PostMapping
    public Result<Void> add(@RequestBody AppType appType,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        // 权限校验：仅管理员可操作
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足，仅管理员可操作");
        }
        appTypeService.save(appType);
        return Result.success();
    }

    /**
     * 编辑应用类型
     * 
     * 接口说明：根据ID更新应用类型信息
     * 权限要求：管理员
     * 
     * @param id 应用类型ID
     * @param appType 更新后的应用类型信息
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id, 
                               @RequestBody AppType appType,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足，仅管理员可操作");
        }
        appType.setId(id);
        appTypeService.updateById(appType);
        return Result.success();
    }

    /**
     * 删除单个应用类型
     * 
     * 接口说明：根据ID删除应用类型
     * 权限要求：管理员
     * 
     * @param id 应用类型ID
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足，仅管理员可操作");
        }
        appTypeService.removeById(id);
        return Result.success();
    }

    /**
     * 批量删除应用类型
     * 
     * 接口说明：根据ID列表批量删除应用类型
     * 权限要求：管理员
     * 
     * @param params 包含ids数组的请求体
     * 请求体示例：{ "ids": [1, 2, 3] }
     */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足，仅管理员可操作");
        }
        List<Integer> ids = params.get("ids");
        appTypeService.batchDelete(ids);
        return Result.success();
    }

    /**
     * 校验是否为管理员
     * 简单实现：检查token是否存在（实际项目中应验证token有效性）
     */
    private boolean checkAdmin(String token) {
        // TODO: 实际项目中应通过AuthService验证token并获取用户角色
        return token != null && !token.isEmpty();
    }
}
