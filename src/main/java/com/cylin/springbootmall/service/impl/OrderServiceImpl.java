package com.cylin.springbootmall.service.impl;

import com.cylin.springbootmall.dao.OrderDao;
import com.cylin.springbootmall.dao.ProductDao;
import com.cylin.springbootmall.dto.BuyItem;
import com.cylin.springbootmall.dto.BuyItemsRequest;
import com.cylin.springbootmall.model.Order;
import com.cylin.springbootmall.model.OrderItem;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Transactional
    @Override
    public int createOrder(int userId, BuyItemsRequest buyItemsRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        for(BuyItem buyItem : buyItemsRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            int price = product.getPrice();
            int amount = buyItem.getQuantity() * price;
            totalAmount += amount;

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct_id(buyItem.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);
            orderItemList.add(orderItem);
        }

        int orderId = orderDao.createOrder(userId, totalAmount);

        orderDao.createItems(orderId, orderItemList);

        return orderId;
    }

    @Override
    public Order getOrderById(int orderId) {
        Order order = orderDao.getOrderById(orderId);

        if(order == null) return order;

        List<OrderItem> list = orderDao.getItemListById(orderId);

        order.setOrderItemList(list);

        return order;
    }
}
