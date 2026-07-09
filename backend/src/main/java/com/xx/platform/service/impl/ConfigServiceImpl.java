package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.PlatformConfig;
import com.xx.platform.mapper.PlatformConfigMapper;
import com.xx.platform.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 平台配置服务实现类
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private PlatformConfigMapper configMapper;

    @Value("${upload.path:./uploads/}")
    private String uploadPath;

    @Override
    public List<PlatformConfig> getAllConfigs() {
        return configMapper.selectList(null);
    }

    @Override
    public String getConfigValue(String key) {
        PlatformConfig config = configMapper.selectOne(
                new LambdaQueryWrapper<PlatformConfig>().eq(PlatformConfig::getConfigKey, key));
        return config != null ? config.getConfigValue() : null;
    }

    @Override
    public void updateConfigs(Map<String, String> configs) {
        for (Map.Entry<String, String> entry : configs.entrySet()) {
            PlatformConfig config = configMapper.selectOne(
                    new LambdaQueryWrapper<PlatformConfig>()
                            .eq(PlatformConfig::getConfigKey, entry.getKey()));
            if (config != null) {
                config.setConfigValue(entry.getValue());
                configMapper.updateById(config);
            }
        }
    }

    @Override
    public String uploadFile(String fileKey, String fileName, byte[] fileBytes) {
        // 确保上传目录存在
        File dir = new File(uploadPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成唯一文件名
        String ext = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : "";
        String newFileName = UUID.randomUUID().toString().replace("-", "") + ext;
        File file = new File(uploadPath, newFileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败：" + e.getMessage());
        }

        // 文件访问路径
        String fileUrl = "/uploads/" + newFileName;

        // 更新配置
        PlatformConfig config = configMapper.selectOne(
                new LambdaQueryWrapper<PlatformConfig>().eq(PlatformConfig::getConfigKey, fileKey));
        if (config != null) {
            config.setConfigValue(fileUrl);
            configMapper.updateById(config);
        }

        return fileUrl;
    }
}
