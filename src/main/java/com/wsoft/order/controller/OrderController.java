package com.wsoft.order.controller;

import com.wsoft.order.controller.dto.ApiResponse;
import com.wsoft.order.controller.dto.OrderResponse;
import com.wsoft.order.controller.dto.PaginationResponse;
import com.wsoft.order.service.OrderService;
import jakarta.websocket.server.PathParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService service) {
        this.orderService = service;
    }

    @GetMapping("/customers/{customerId}/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> listOrders(@PathVariable("customerId") Long customerId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name =  "pageSize", defaultValue = "100") Integer pageSize) {
        var body = orderService.findAllByCustomerId(customerId, PageRequest.of(page, pageSize));
        var totalOnOrders = orderService.findTotalOnOrdersByCustomerId(customerId);
        return ResponseEntity.ok(new ApiResponse<>(
                Map.of("totalOnOrders", totalOnOrders),
                body.getContent(),
                PaginationResponse.fromPage(body)));
    }
}
