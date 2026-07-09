package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 宣贯数据实体类
 * 包含：用户生态、产品体系、模型体系、数据体系、知识产权
 */
@Data
@TableName("showcase_item")
public class ShowcaseItem {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 类别：USER_ECOLOGY/PRODUCT_SYSTEM/MODEL_SYSTEM/DATA_SYSTEM/IP */
    private String category;

    /** 标题 */
    private String title;

    /** 概览摘要 */
    private String summary;

    /** 详细内容 */
    private String content;

    /** 排序序号 */
    private Integer sortOrder;

    private Date createTime;

    private Date updateTime;
}
