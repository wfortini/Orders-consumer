package com.wsoft.order.repository;

import com.wsoft.order.controller.dto.OrderResponse;
import com.wsoft.order.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCustomerId(Long id, PageRequest pageRequest);
}
