package com.cylin.springbootmall.service.impl;

import com.cylin.springbootmall.constant.Status;
import com.cylin.springbootmall.dao.impl.ProductDaoImpl;
import com.cylin.springbootmall.dto.ProductRequest;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDaoImpl productDaoImpl;

    @Override
    public List<Product> getProducts() {
        return productDaoImpl.getProducts();
    }

    @Override
    public Product getProductById(Integer productId) {

        return productDaoImpl.getProductById(productId);
    }

    @Override
    public int createProduct(ProductRequest productRequest) {
        return productDaoImpl.createProduct(productRequest);
    }

    @Override
    public Status updateProduct(int productId, ProductRequest productRequest) {
        return productDaoImpl.updateProduct(productId, productRequest);
    }

    @Override
    public void deleteProductById(int productId) {
        productDaoImpl.deleteProductById(productId);
    }
}
