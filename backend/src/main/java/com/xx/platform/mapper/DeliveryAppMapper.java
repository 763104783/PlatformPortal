package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.DeliveryApp;
import org.apache.ibatis.annotations.Mapper;

/**
 * 交付应用Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得CRUD能力。
 * 对应数据库表：delivery_app
 */
@Mapper
public interface DeliveryAppMapper extends BaseMapper<DeliveryApp> {
}
