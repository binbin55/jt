package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	
	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderShippingMapper orderShippingMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Transactional
	@Override
	public String saveObject(Order order) {
		String orderId = "" + order.getUserId() + System.currentTimeMillis();
		//实现订单入库
		Date date = new Date();
		order.setStatus(1).setOrderId(orderId).setCreated(date).setUpdated(date);
		orderMapper.insert(order);
		//订单物流
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId).setCreated(date).setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		//4.入库订单商品
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem: orderItems){
			orderItem.setOrderId(orderId).setCreated(date).setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		return orderId;
	}

	/**
	 * 利用id查询订单的全部信息
	 * @param id
	 * @return
	 */
	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping orderShippin = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id",id);
		List<OrderItem> list = orderItemMapper. selectList(queryWrapper);
		order.setOrderShipping(orderShippin).setOrderItems(list);
		return order;
	}
}
