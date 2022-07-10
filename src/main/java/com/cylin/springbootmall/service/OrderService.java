package com.cylin.springbootmall.service;

import com.cylin.springbootmall.dto.BuyItemsRequest;
import com.cylin.springbootmall.dto.OrderQueryParam;
import com.cylin.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    int createOrder(int userId, BuyItemsRequest buyItemsRequest);

    Order getOrderById(int orderId);

    List<Order> getOrders(int userId, OrderQueryParam orderQueryParam);

    int countOrder(int userId);
}
