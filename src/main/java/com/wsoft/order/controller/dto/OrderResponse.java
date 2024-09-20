package com.wsoft.order.controller.dto;

import com.wsoft.order.entity.OrderEntity;
import com.wsoft.order.repository.OrderRepository;

import java.math.BigDecimal;

public record OrderResponse(Long orderId, Long customerId, BigDecimal total) {

    public static OrderResponse fromEntity(OrderEntity entity){
        return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
    }
}
