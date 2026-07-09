package com.xx.platform.controller;

import com.xx.platform.common.Result;
import com.xx.platform.entity.PlatformConfig;
import com.xx.platform.entity.SysUser;
import com.xx.platform.service.AuthService;
import com.xx.platform.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 平台配置控制器
 * 管理平台的名称、Logo、公司名称、底图等配置
 */
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private AuthService authService;

    /**
     * 获取所有平台配置
     * GET /api/config
     */
    @GetMapping
    public Result<List<PlatformConfig>> getAll() {
        return Result.success(configService.getAllConfigs());
    }

    /**
     * 批量更新配置
     * PUT /api/config
     * @param configs key-value对
     */
    @PutMapping
    public Result<Void> update(@RequestBody Map<String, String> configs,
                               @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        configService.updateConfigs(configs);
        return Result.success();
    }

    /**
     * 上传文件（Logo/底图）
     * POST /api/config/upload
     * @param file 上传的文件
     * @param fileKey 配置key（logo_path 或 bg_image）
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam("fileKey") String fileKey,
                                 @RequestHeader(value = "Authorization", required = false) String token) {
        checkAdmin(token);
        try {
            String url = configService.uploadFile(fileKey, file.getOriginalFilename(), file.getBytes());
            return Result.success(url);
        } catch (Exception e) {
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    private void checkAdmin(String token) {
        if (token == null) throw new RuntimeException("请先登录");
        SysUser user = authService.getUserByToken(token);
        if (user == null || !"ADMIN".equals(user.getRole())) throw new RuntimeException("无管理员权限");
    }
}
