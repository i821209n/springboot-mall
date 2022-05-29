package com.cylin.springbootmall.service.impl;

import com.cylin.springbootmall.dao.impl.ProductDaoImpl;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDaoImpl productDaoImpl;

    @Override
    public Product getProductById(Integer productId) {

        return productDaoImpl.getProductById(productId);
    }
}
