package com.xx.platform.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Web应用实体类
 */
@Data
@TableName("web_app")
public class WebApp {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /** 应用名称 */
    private String name;

    /** 功能简介 */
    private String description;

    /** 分类ID */
    private Integer categoryId;

    /** 封面图片路径 */
    private String coverImage;

    /** 版本号 */
    private String version;

    /** 详细介绍 */
    private String detail;

    /** 应用链接 */
    private String url;

    /** 点击次数 */
    private Integer clickCount;

    /** 排序序号 */
    private Integer sortOrder;

    /** 状态：1启用 0禁用 */
    private Integer status;

    private Date createTime;

    private Date updateTime;
}
