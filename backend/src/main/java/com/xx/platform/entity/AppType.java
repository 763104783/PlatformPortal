package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 应用类型实体类（枚举量）
 */
@Data
@TableName("app_type")
public class AppType {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 应用类型名称 */
    private String name;

    /** 简介 */
    private String description;

    /** 排序号 */
    private Integer sortOrder;

    /** 创建时间 */
    private String createTime;
}
