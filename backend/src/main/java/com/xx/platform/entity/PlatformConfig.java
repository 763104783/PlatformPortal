package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 平台配置实体类
 * 存储平台名称、Logo、公司名称、底图等可配置项
 */
@Data
@TableName("platform_config")
public class PlatformConfig {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 配置键 */
    private String configKey;

    /** 配置值 */
    private String configValue;

    /** 配置描述 */
    private String description;
}
