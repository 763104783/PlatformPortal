package com.xx.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xx.platform.common.Result;
import com.xx.platform.entity.DeliveryApp;
import com.xx.platform.service.DeliveryAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 交付应用控制器
 * 
 * 提供交付应用（业务APP）的REST API接口：
 * 
 * 【前台展示接口】
 * - GET  /api/app/page          分页查询启用的应用（公开，支持搜索/筛选/排序）
 * - GET  /api/app/{id}          获取应用详情（公开）
 * - POST /api/app/{id}/click    记录点击次数（公开）
 * 
 * 【后台管理接口】
 * - GET    /api/app/admin/page    分页查询所有应用（管理员）
 * - POST   /api/app               新增应用（管理员）
 * - PUT    /api/app/{id}          编辑应用（管理员）
 * - DELETE /api/app/{id}          删除应用（管理员）
 * - DELETE /api/app/batch         批量删除应用（管理员）
 */
@RestController
@RequestMapping("/api/app")
public class DeliveryAppController {

    @Autowired
    private DeliveryAppService deliveryAppService;

    /**
     * 分页查询交付应用（前台展示用）
     * 只返回状态为启用的应用
     * 
     * @param page 页码（默认1）
     * @param size 每页条数（默认10）
     * @param keyword 搜索关键词（匹配名称或简介）
     * @param typeId 应用类型ID筛选（可选）
     * @param sortField 排序字段：clickCount(点击数)/name(名称)/sortOrder(排序号，默认)
     */
    @GetMapping("/page")
    public Result<IPage<DeliveryApp>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer typeId,
            @RequestParam(required = false) String sortField) {
        return Result.success(deliveryAppService.pageQuery(page, size, keyword, typeId, sortField));
    }

    /**
     * 分页查询交付应用（后台管理用）
     * 返回所有状态的应用，供管理员管理
     */
    @GetMapping("/admin/page")
    public Result<IPage<DeliveryApp>> adminPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer typeId,
            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        return Result.success(deliveryAppService.pageQueryAll(page, size, keyword, typeId));
    }

    /** 获取应用详情（含关联的应用类型和业务流程） */
    @GetMapping("/{id}")
    public Result<DeliveryApp> detail(@PathVariable Integer id) {
        return Result.success(deliveryAppService.getDetail(id));
    }

    /**
     * 记录应用点击次数
     * 用户点击应用卡片时调用，点击数+1
     */
    @PostMapping("/{id}/click")
    public Result<Void> click(@PathVariable Integer id) {
        deliveryAppService.incrementClickCount(id);
        return Result.success();
    }

    /**
     * 新增交付应用
     * 请求体中可包含processIds数组指定关联的业务流程
     * 示例：{ "name": "xxx", "url": "http://...", "appTypeId": 1, "processIds": [1, 2] }
     */
    @PostMapping
    public Result<Void> add(@RequestBody DeliveryApp app,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        deliveryAppService.addApp(app);
        return Result.success();
    }

    /**
     * 编辑交付应用
     * 请求体中可包含processIds数组更新关联的业务流程
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id,
                               @RequestBody DeliveryApp app,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        app.setId(id);
        deliveryAppService.updateApp(app);
        return Result.success();
    }

    /** 删除单个交付应用（同时删除流程关联） */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        deliveryAppService.batchDelete(Collections.singletonList(id));
        return Result.success();
    }

    /** 批量删除交付应用 */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        deliveryAppService.batchDelete(params.get("ids"));
        return Result.success();
    }

    private boolean checkAdmin(String token) {
        return token != null && !token.isEmpty();
    }
}
