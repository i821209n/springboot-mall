package com.cylin.springbootmall.service;

import com.cylin.springbootmall.constant.Status;
import com.cylin.springbootmall.dto.ProductRequest;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.model.ProductQueryParam;

import java.util.List;

public interface ProductService {

    List<Product> getProducts(ProductQueryParam productQueryParam);

    Product getProductById(Integer productId);

    int createProduct(ProductRequest productRequest);

    Status updateProduct(int productId, ProductRequest productRequest);

    void deleteProductById(int productId);
}
