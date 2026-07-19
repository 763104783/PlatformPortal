package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据实体模型（实体量）
 */
@Data
@TableName("data_model")
public class DataModel {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 模型名称 */
    private String name;

    /** 简介 */
    private String description;

    /** 制作人 */
    private String author;

    /** 版本号 */
    private String version;

    /** 制作时间 */
    private String createTime;

    /** 更新时间 */
    private String updateTime;
}
