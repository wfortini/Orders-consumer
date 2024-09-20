package com.wsoft.order.listener.dto;

import java.math.BigDecimal;

public record OrderItemEvent(String product, Integer quantity,
                              BigDecimal price) {
}
