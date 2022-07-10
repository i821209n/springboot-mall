package com.cylin.springbootmall.service.impl;

import com.cylin.springbootmall.dao.OrderDao;
import com.cylin.springbootmall.dao.ProductDao;
import com.cylin.springbootmall.dao.UserDao;
import com.cylin.springbootmall.dto.BuyItem;
import com.cylin.springbootmall.dto.BuyItemsRequest;
import com.cylin.springbootmall.model.Order;
import com.cylin.springbootmall.model.OrderItem;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.model.User;
import com.cylin.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Transactional
    @Override
    public int createOrder(int userId, BuyItemsRequest buyItemsRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItemList = new ArrayList<>();

        checkUser(userId);

        for(BuyItem buyItem : buyItemsRequest.getBuyItemList()){
            Product product = productDao.getProductById(buyItem.getProductId());

            checkProduct(product, buyItem.getQuantity(), buyItem.getProductId());

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

    private void checkUser(int userId){
        User user = userDao.getUserById(userId);

        if(user == null){
            log.warn("user : {} is not exist", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private void checkProduct(Product product, int quantity, int productId){
        if(product == null) {
            log.warn("product : {} is not exist", productId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else if(product.getStock() < quantity){
            log.warn("the stock of the product {} is not enough. stock : {} wanted : {}",product.getProduct_id(), product.getStock(), quantity);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } else {
            productDao.updateStock(product.getProduct_id(), product.getStock() - quantity);
        }
    }
}
