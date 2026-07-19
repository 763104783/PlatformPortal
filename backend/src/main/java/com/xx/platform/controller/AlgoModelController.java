package com.xx.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xx.platform.common.Result;
import com.xx.platform.entity.AlgoModel;
import com.xx.platform.service.AlgoModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 算法能力模型控制器
 * 
 * 提供算法能力模型的REST API接口：
 * - GET    /api/algo-model/page     分页查询（公开，支持关键词搜索）
 * - GET    /api/algo-model/{id}     获取详情（公开）
 * - POST   /api/algo-model          新增（管理员）
 * - PUT    /api/algo-model/{id}     编辑（管理员）
 * - DELETE /api/algo-model/{id}     删除（管理员）
 * - DELETE /api/algo-model/batch    批量删除（管理员）
 */
@RestController
@RequestMapping("/api/algo-model")
public class AlgoModelController {

    @Autowired
    private AlgoModelService algoModelService;

    /** 分页查询算法能力模型 */
    @GetMapping("/page")
    public Result<IPage<AlgoModel>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return Result.success(algoModelService.pageQuery(page, size, keyword));
    }

    /** 获取算法能力模型详情 */
    @GetMapping("/{id}")
    public Result<AlgoModel> detail(@PathVariable Integer id) {
        return Result.success(algoModelService.getById(id));
    }

    /** 新增算法能力模型 */
    @PostMapping
    public Result<Void> add(@RequestBody AlgoModel model,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        algoModelService.save(model);
        return Result.success();
    }

    /** 编辑算法能力模型 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id,
                               @RequestBody AlgoModel model,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        model.setId(id);
        algoModelService.updateById(model);
        return Result.success();
    }

    /** 删除单个算法能力模型 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        algoModelService.removeById(id);
        return Result.success();
    }

    /** 批量删除算法能力模型 */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        algoModelService.batchDelete(params.get("ids"));
        return Result.success();
    }

    private boolean checkAdmin(String token) {
        return token != null && !token.isEmpty();
    }
}
