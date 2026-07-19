package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.BizProcess;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务流程Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得CRUD能力。
 * 对应数据库表：biz_process
 */
@Mapper
public interface BizProcessMapper extends BaseMapper<BizProcess> {
}
