package com.xx.platform.service;

import com.xx.platform.entity.PlatformConfig;

import java.util.List;
import java.util.Map;

/**
 * 平台配置服务接口
 */
public interface ConfigService {

    /**
     * 获取所有配置
     */
    List<PlatformConfig> getAllConfigs();

    /**
     * 根据key获取配置值
     */
    String getConfigValue(String key);

    /**
     * 批量更新配置
     * @param configs key-value对
     */
    void updateConfigs(Map<String, String> configs);

    /**
     * 上传文件（Logo/底图等），将图片转为Base64 Data URL存入数据库
     * @param fileKey 配置key
     * @param fileName 文件名（用于推断MIME类型）
     * @param fileBytes 文件内容
     * @return Base64 Data URL字符串
     */
    String uploadFile(String fileKey, String fileName, byte[] fileBytes);
}
