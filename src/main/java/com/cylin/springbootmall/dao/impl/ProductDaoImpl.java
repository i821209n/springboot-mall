package com.cylin.springbootmall.dao.impl;

import com.cylin.springbootmall.constant.Status;
import com.cylin.springbootmall.dao.ProductDao;
import com.cylin.springbootmall.dto.ProductRequest;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.model.ProductQueryParam;
import com.cylin.springbootmall.rowMapper.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.cylin.springbootmall.constant.Status.STATUS_SUCCESS;

@Component
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Product> getProducts(ProductQueryParam productQueryParam) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date " +
                "FROM product " +
                "WHERE 1=1 ";

        Map<String, Object> map = new HashMap<>();

        if(productQueryParam.getCategory() != null){
            sql = sql + " AND category = :category ";
            map.put("category", productQueryParam.getCategory().name());
        }

        if(productQueryParam.getSearch() != null){
            sql = sql + " AND product_name LIKE :search ";
            map.put("search", "%" + productQueryParam.getSearch() + "%");
        }

        return namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
    }

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date " +
                "FROM product " +
                "WHERE product_id = :product_id";

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", productId);

        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());

        if(productList.size() == 0) {
            return null;
        } else {
            return productList.get(0);
        }
    }

    @Override
    public int createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date) " +
                "VALUES (:product_name, :category, :image_url, :price, :stock, :description, :created_date, :last_modified_date)";

        Map<String, Object> map = new HashMap<>();
        map.put("product_name", productRequest.getProduct_name());
        map.put("category", productRequest.getCategory().name());
        map.put("image_url", productRequest.getImage_url());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date cur = new Date();
        map.put("created_date", cur);
        map.put("last_modified_date", cur);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        return  Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public Status updateProduct(int productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :product_name, category = :category, image_url = :image_url," +
                " price = :price, stock = :stock, description = :description, last_modified_date = :last_modified_date " +
                "WHERE product_id = :product_id";

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", productId);
        map.put("product_name", productRequest.getProduct_name());
        map.put("category", productRequest.getCategory().name());
        map.put("image_url", productRequest.getImage_url());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date cur = new Date();
        map.put("last_modified_date", cur);

        namedParameterJdbcTemplate.update(sql, map);

        return STATUS_SUCCESS;
    }

    @Override
    public void deleteProductById(int productId) {
        String sql = "DELETE FROM product WHERE product_id = :product_id";

        Map<String, Object> map = new HashMap<>();
        map.put("product_id", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
