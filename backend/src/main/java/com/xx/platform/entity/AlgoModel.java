package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 算法能力模型实体类（实体量）
 * 
 * 用于描述平台中的算法功能模型，由模型算法工程师维护。
 * 对应数据库表：algo_model
 */
@Data
@TableName("algo_model")
public class AlgoModel {

    /** 主键ID，自增 */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 算法模型名称，如"图像识别模型"、"NLP分析模型" */
    private String name;

    /** 模型简介，描述模型的功能和用途 */
    private String description;

    /** 制作人，记录模型的创建/维护者 */
    private String author;

    /** 版本号，如"1.0.0"、"2.1.3" */
    private String version;

    /** 制作时间，格式：yyyy-MM-dd HH:mm:ss */
    private String createTime;

    /** 最后更新时间，格式：yyyy-MM-dd HH:mm:ss */
    private String updateTime;
}
