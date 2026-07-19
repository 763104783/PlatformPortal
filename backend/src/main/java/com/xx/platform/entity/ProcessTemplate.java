package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 业务流程模板实体类（枚举量）
 */
@Data
@TableName("process_template")
public class ProcessTemplate {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 模板名称 */
    private String name;

    /** 简介 */
    private String description;

    /** 排序号 */
    private Integer sortOrder;

    /** 创建时间 */
    private String createTime;
}
