package com.xx.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xx.platform.common.Result;
import com.xx.platform.entity.DataModel;
import com.xx.platform.service.DataModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 数据实体模型控制器
 * 
 * 提供数据实体模型的REST API接口：
 * - GET    /api/data-model/page     分页查询（公开，支持关键词搜索）
 * - GET    /api/data-model/{id}     获取详情（公开）
 * - POST   /api/data-model          新增（管理员）
 * - PUT    /api/data-model/{id}     编辑（管理员）
 * - DELETE /api/data-model/{id}     删除（管理员）
 * - DELETE /api/data-model/batch    批量删除（管理员）
 */
@RestController
@RequestMapping("/api/data-model")
public class DataModelController {

    @Autowired
    private DataModelService dataModelService;

    /**
     * 分页查询数据实体模型
     * @param page 页码（默认1）
     * @param size 每页条数（默认10）
     * @param keyword 搜索关键词（可选）
     */
    @GetMapping("/page")
    public Result<IPage<DataModel>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(dataModelService.pageQuery(page, size, keyword));
    }

    /** 获取数据实体模型详情 */
    @GetMapping("/{id}")
    public Result<DataModel> detail(@PathVariable Integer id) {
        return Result.success(dataModelService.getById(id));
    }

    /** 新增数据实体模型 */
    @PostMapping
    public Result<Void> add(@RequestBody DataModel model,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        dataModelService.save(model);
        return Result.success();
    }

    /** 编辑数据实体模型 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id,
                               @RequestBody DataModel model,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        model.setId(id);
        dataModelService.updateById(model);
        return Result.success();
    }

    /** 删除单个数据实体模型 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        dataModelService.removeById(id);
        return Result.success();
    }

    /** 批量删除数据实体模型 */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        dataModelService.batchDelete(params.get("ids"));
        return Result.success();
    }

    private boolean checkAdmin(String token) {
        return token != null && !token.isEmpty();
    }
}
