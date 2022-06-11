package com.cylin.springbootmall.dao;

import com.cylin.springbootmall.constant.ProductCategory;
import com.cylin.springbootmall.constant.Status;
import com.cylin.springbootmall.dto.ProductRequest;
import com.cylin.springbootmall.model.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getProducts(ProductCategory category, String search);

    Product getProductById(Integer productId);

    int createProduct(ProductRequest productRequest);

    Status updateProduct(int productId, ProductRequest productRequest);

    void deleteProductById(int productId);
}
