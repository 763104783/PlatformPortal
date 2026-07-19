package com.xx.platform.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xx.platform.entity.AlgoModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * 算法能力模型Mapper接口
 * 
 * 继承MyBatis-Plus的BaseMapper，自动获得CRUD能力。
 * 对应数据库表：algo_model
 */
@Mapper
public interface AlgoModelMapper extends BaseMapper<AlgoModel> {
}
