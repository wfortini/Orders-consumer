package com.wsoft.order.service;

import com.wsoft.order.controller.dto.OrderResponse;
import com.wsoft.order.entity.OrderEntity;
import com.wsoft.order.entity.OrderItem;
import com.wsoft.order.listener.dto.OrderCreatedEvent;
import com.wsoft.order.repository.OrderRepository;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MongoTemplate mongoTemplate;

    public OrderService(OrderRepository repo, MongoTemplate template) {
        this.orderRepository = repo;
        this.mongoTemplate = template;
    }

    public void save(OrderCreatedEvent event) {

        var entity = new OrderEntity();
        entity.setOrderId(event.orderId());
        entity.setCustomerId(event.customerId());
        entity.setItens(event.items().stream()
                .map(i -> new OrderItem(i.product(), i.quantity(), i.price())).toList());
        entity.setTotal(getTotal(event));

        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long id, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(id, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    public BigDecimal findTotalOnOrdersByCustomerId(Long customerId) {
           var aggregation = newAggregation(match(Criteria.where("customerId").is(customerId)),
                   group().sum("total").as("total"));
           var response =  mongoTemplate.aggregate(aggregation, "tb_orders", Document.class);
           return new BigDecimal(response.getUniqueMappedResult().get("total").toString());
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.items()
                .stream()
                .map(i -> i.price().multiply(BigDecimal.valueOf(i.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO.stripTrailingZeros());
    }
}
