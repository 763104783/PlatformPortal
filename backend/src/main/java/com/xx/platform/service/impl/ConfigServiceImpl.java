package com.xx.platform.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xx.platform.entity.PlatformConfig;
import com.xx.platform.mapper.PlatformConfigMapper;
import com.xx.platform.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * 平台配置服务实现类
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    private PlatformConfigMapper configMapper;

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

    /**
     * 上传文件（Logo/底图等），将图片转为Base64 Data URL存入数据库
     * @param fileKey 配置key
     * @param fileName 文件名（用于推断MIME类型）
     * @param fileBytes 文件内容
     * @return Base64 Data URL字符串
     */
    @Override
    public String uploadFile(String fileKey, String fileName, byte[] fileBytes) {
        // 根据文件名推断MIME类型
        String mimeType = getMimeType(fileName);

        // 将文件字节转为Base64编码
        String base64 = Base64.getEncoder().encodeToString(fileBytes);

        // 拼接为Data URL格式，可直接用于<img src>
        String dataUrl = "data:" + mimeType + ";base64," + base64;

        // 将Base64 Data URL存入平台配置表
        PlatformConfig config = configMapper.selectOne(
                new LambdaQueryWrapper<PlatformConfig>().eq(PlatformConfig::getConfigKey, fileKey));
        if (config != null) {
            config.setConfigValue(dataUrl);
            configMapper.updateById(config);
        }

        return dataUrl;
    }

    /**
     * 根据文件名推断MIME类型
     * @param fileName 文件名
     * @return MIME类型字符串
     */
    private String getMimeType(String fileName) {
        if (fileName == null) return "image/png";
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".gif")) return "image/gif";
        if (lower.endsWith(".webp")) return "image/webp";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        if (lower.endsWith(".ico")) return "image/x-icon";
        return "image/png"; // 默认返回png类型
    }
}
