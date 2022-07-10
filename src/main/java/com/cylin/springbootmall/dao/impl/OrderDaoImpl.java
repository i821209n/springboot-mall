package com.cylin.springbootmall.dao.impl;

import com.cylin.springbootmall.dao.OrderDao;
import com.cylin.springbootmall.dto.OrderQueryParam;
import com.cylin.springbootmall.model.Order;
import com.cylin.springbootmall.model.OrderItem;
import com.cylin.springbootmall.rowMapper.OrderItemRowMapper;
import com.cylin.springbootmall.rowMapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int createOrder(int userId, int totalAmount) {
        String sql = "INSERT INTO `order` (user_id, total_amount, created_date, last_modified_date) " +
                "VALUES (:user_id, :total_amount, :created_date, :last_modified_date)";

        Map<String, Object> map = new HashMap<>();

        map.put("user_id", userId);
        map.put("total_amount", totalAmount);

        Date cur = new Date();
        map.put("created_date", cur);
        map.put("last_modified_date", cur);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return keyHolder.getKey().intValue();
    }

    @Override
    public void createItems(int orderId, List<OrderItem> orderItemList) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, amount) " +
                "VALUES (:order_id, :product_id, :quantity, :amount)";

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for(int i = 0; i < orderItemList.size(); i++){
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("order_id", orderId);
            parameterSources[i].addValue("product_id", orderItem.getProduct_id());
            parameterSources[i].addValue("quantity", orderItem.getQuantity());
            parameterSources[i].addValue("amount", orderItem.getAmount());
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
    }

    @Override
    public Order getOrderById(int orderId) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM `order` " +
                "WHERE order_id = :order_id";

        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);

        List<Order> list = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        if(list.size() == 0){
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public List<OrderItem> getItemListById(int orderId) {
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, " +
                "p.product_name, p.image_url " +
                "FROM order_item oi JOIN product p on oi.product_id = p.product_id " +
                "WHERE order_id = :order_id";

        Map<String, Object> map = new HashMap<>();
        map.put("order_id", orderId);

        return namedParameterJdbcTemplate.query(sql, map, new OrderItemRowMapper());
    }

    @Override
    public List<Order> getOrders(int userId, OrderQueryParam orderQueryParam) {
        String sql = "SELECT order_id, user_id, total_amount, created_date, last_modified_date " +
                "FROM `order` " +
                "WHERE 1 = 1";

        Map<String, Object> map = new HashMap<>();

        sql = sql + " AND user_id = :user_id ";
        sql = sql + " ORDER BY created_date desc ";
        sql = sql + " LIMIT :limit OFFSET :offset ";

        map.put("user_id", userId);
        map.put("limit", orderQueryParam.getLimit());
        map.put("offset", orderQueryParam.getOffset());

        return namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());
    }

    @Override
    public int countOrder(int userId) {
        String sql = "SELECT count(*) " +
                "FROM `order` " +
                "WHERE user_id = :user_id";

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);

        return namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
    }
}
