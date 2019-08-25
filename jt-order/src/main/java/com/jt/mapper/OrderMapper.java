package com.jt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper extends BaseMapper<Order>{

    Order findOrderById(@Param("id")Integer id);

}