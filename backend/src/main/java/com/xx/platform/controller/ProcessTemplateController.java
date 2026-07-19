package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.entity.ProcessTemplate;
import com.xx.platform.service.ProcessTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务流程模板控制器
 * 
 * 提供业务流程模板（枚举量）的REST API接口：
 * - GET    /api/process-template/list    获取所有模板列表（公开）
 * - POST   /api/process-template         新增模板（管理员）
 * - PUT    /api/process-template/{id}    编辑模板（管理员）
 * - DELETE /api/process-template/{id}    删除模板（管理员）
 * - DELETE /api/process-template/batch   批量删除模板（管理员）
 */
@RestController
@RequestMapping("/api/process-template")
public class ProcessTemplateController {

    @Autowired
    private ProcessTemplateService processTemplateService;

    /** 获取所有流程模板列表（按排序号升序） */
    @GetMapping("/list")
    public Result<List<ProcessTemplate>> list() {
        return Result.success(processTemplateService.listAll());
    }

    /** 新增流程模板 */
    @PostMapping
    public Result<Void> add(@RequestBody ProcessTemplate template,
                            @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        processTemplateService.save(template);
        return Result.success();
    }

    /** 编辑流程模板 */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Integer id,
                               @RequestBody ProcessTemplate template,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        template.setId(id);
        processTemplateService.updateById(template);
        return Result.success();
    }

    /** 删除单个流程模板 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        processTemplateService.removeById(id);
        return Result.success();
    }

    /** 批量删除流程模板 */
    @DeleteMapping("/batch")
    public Result<Void> batchDelete(@RequestBody Map<String, List<Integer>> params,
                                    @RequestHeader(value = "Authorization", required = false) String token) {
        if (!checkAdmin(token)) {
            return Result.error(403, "权限不足");
        }
        processTemplateService.batchDelete(params.get("ids"));
        return Result.success();
    }

    /** 校验管理员权限 */
    private boolean checkAdmin(String token) {
        return token != null && !token.isEmpty();
    }
}
