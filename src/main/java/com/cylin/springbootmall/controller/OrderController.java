package com.cylin.springbootmall.controller;

import com.cylin.springbootmall.dto.BuyItemsRequest;
import com.cylin.springbootmall.dto.OrderQueryParam;
import com.cylin.springbootmall.model.Order;
import com.cylin.springbootmall.service.OrderService;
import com.cylin.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Validated
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

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<Page<Order>> getOrders(@PathVariable int userId,
                                                 @RequestParam(defaultValue = "10") @Max(100) @Min(0) int limit,
                                                 @RequestParam(defaultValue = "0") @Min(0) int offset){

        OrderQueryParam orderQueryParam = new OrderQueryParam();
        orderQueryParam.setLimit(limit);
        orderQueryParam.setOffset(offset);

        List<Order> list = orderService.getOrders(userId, orderQueryParam);

        int total = orderService.countOrder(userId);

        Page<Order> res = new Page<>();

        res.setLimit(limit);
        res.setOffset(offset);
        res.setTotal(total);
        res.setResult(list);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
