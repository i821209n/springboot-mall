package com.cylin.springbootmall.controller;

import com.cylin.springbootmall.dto.BuyItemsRequest;
import com.cylin.springbootmall.model.Order;
import com.cylin.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable int userId,
                                         @RequestBody @Valid BuyItemsRequest buyItemsRequest){

        int orderId = orderService.createOrder(userId, buyItemsRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);

    }
}
