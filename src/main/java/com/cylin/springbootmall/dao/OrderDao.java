package com.cylin.springbootmall.dao;

import com.cylin.springbootmall.dto.OrderQueryParam;
import com.cylin.springbootmall.model.Order;
import com.cylin.springbootmall.model.OrderItem;

import java.util.List;

public interface OrderDao {

    int createOrder(int userId, int totalAmount);

    void createItems(int orderId, List<OrderItem> orderItemList);

    Order getOrderById(int orderId);

    List<OrderItem> getItemListById(int orderId);

    List<Order> getOrders(int userId, OrderQueryParam orderQueryParam);

    int countOrder(int userId);
}
