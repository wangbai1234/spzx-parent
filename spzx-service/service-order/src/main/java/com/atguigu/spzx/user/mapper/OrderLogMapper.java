package com.atguigu.spzx.user.mapper;

import com.atguigu.spzx.model.entity.order.OrderLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderLogMapper {
    void save(OrderLog orderLog);
}
