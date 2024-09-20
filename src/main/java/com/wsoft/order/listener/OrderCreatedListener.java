package com.wsoft.order.listener;

import com.wsoft.order.listener.dto.OrderCreatedEvent;
import com.wsoft.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import static com.wsoft.order.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

@Component
public class OrderCreatedListener  {

    private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);
    private final OrderService orderService;

    public OrderCreatedListener(OrderService service) {
         this.orderService = service;
    }
    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listener(Message<OrderCreatedEvent> msg){
        logger.info("Message consumer: {} ", msg);
        orderService.save(msg.getPayload());
    }
}
