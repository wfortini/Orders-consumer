package com.wsoft.order.listener.dto;

import com.wsoft.order.entity.OrderItem;

import java.util.List;

public record OrderCreatedEvent(Long orderId, Long customerId,
                                List<OrderItemEvent> items) {
}
