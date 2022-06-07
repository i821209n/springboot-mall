package com.cylin.springbootmall.service;

import com.cylin.springbootmall.dto.ProductRequest;
import com.cylin.springbootmall.model.Product;

public interface ProductService {

    Product getProductById(Integer productId);

    int createProduct(ProductRequest productRequest);
}
