package com.cylin.springbootmall.service;

import com.cylin.springbootmall.dto.BuyItemsRequest;
import com.cylin.springbootmall.model.Order;

public interface OrderService {

    int createOrder(int userId, BuyItemsRequest buyItemsRequest);

    Order getOrderById(int orderId);
}
