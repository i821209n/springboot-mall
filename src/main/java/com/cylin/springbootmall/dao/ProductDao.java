package com.cylin.springbootmall.dao;

import com.cylin.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
