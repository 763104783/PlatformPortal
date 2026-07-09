package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 应用分类实体类
 */
@Data
@TableName("app_category")
public class AppCategory {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 分类名称 */
    private String name;

    /** 排序序号 */
    private Integer sortOrder;

    private Date createTime;
}
