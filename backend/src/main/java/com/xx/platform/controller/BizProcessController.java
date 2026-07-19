package com.xx.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xx.platform.common.Result;
import com.xx.platform.entity.BizProcess;
import com.xx.platform.service.BizProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 业务流程控制器
 * 
 * 提供业务流程的REST API接口：
 * - GET    /api/process/page     分页查询（公开，含关联模板信息）
 * - GET    /api/process/{id}     获取详情（公开，含关联模板列表）
 * - POST   /api/process          新增（管理员，同时保存模板关联）
 * - PUT    /api/process/{id}     编辑（管理员，同时更新模板关联）
 * - DELETE /api/process/{id}     删除（管理员，同时删除关联）
 * - DELETE /api/process/batch    批量删除（管理员）
 */
@RestController
@RequestMapping("/api/process")
public class BizProcessController {

    @Autowired
    private BizProcessService bizProcessService;

    /**
     * 分页查询业务流程
     * 返回结果中每条记录包含关联的模板列表(templates字段)
     */
    @GetMapping("/page")
    public Result<IPage<BizProcess>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(bizProcessService.pageQuery(page, size, keyword));
    }

    /** 获取业务流程详情（含关联模板列表） */
    @GetMapping("/{id}")
    public Result<BizProcess> detail(@PathVariable Integer id) {
        return Result.success(bizProcessService.getDetail(id));
    }

    /**
     * 新增业务流程
     * 请求体中可包含templateIds数组指定关联的模板
     * 示例：{ "name": "xxx", "templateIds": [1, 2] }
     */
    @PostMapping
    public Result<Void> add(@RequestBody BizProcess process,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        bizProcessService.addProcess(process);
        return Result.success();
    }

    /**
     * 编辑业务流程
     * 请求体中可包含templateIds数组更新关联的模板
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id,
                               @RequestBody BizProcess process,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        process.setId(id);
        bizProcessService.updateProcess(process);
        return Result.success();
    }

    /** 删除单个业务流程（同时删除模板关联） */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        bizProcessService.batchDelete(Collections.singletonList(id));
        return Result.success();
    }

    /** 批量删除业务流程 */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        bizProcessService.batchDelete(params.get("ids"));
        return Result.success();
    }

    private boolean checkAdmin(String token) {
        return token != null && !token.isEmpty();
    }
}
