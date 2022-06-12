package com.cylin.springbootmall.controller;

import com.cylin.springbootmall.constant.ProductCategory;
import com.cylin.springbootmall.dto.ProductRequest;
import com.cylin.springbootmall.model.Product;
import com.cylin.springbootmall.model.ProductQueryParam;
import com.cylin.springbootmall.service.impl.ProductServiceImpl;
import com.cylin.springbootmall.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static com.cylin.springbootmall.constant.Status.STATUS_SUCCESS;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(@RequestParam(required = false) ProductCategory category,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(defaultValue = "created_date") String orderBy,
                                                     @RequestParam(defaultValue = "desc") String sort,
                                                     @RequestParam(defaultValue = "5") @Max(1000) @Min(0) int limit,
                                                     @RequestParam(defaultValue = "0") @Min(0) int offset){
        ProductQueryParam productQueryParam = new ProductQueryParam();
        productQueryParam.setCategory(category);
        productQueryParam.setSearch(search);
        productQueryParam.setOrderBy(orderBy);
        productQueryParam.setSort(sort);
        productQueryParam.setLimit(limit);
        productQueryParam.setOffset(offset);

        List<Product> list = productService.getProducts(productQueryParam);

        int total = productService.countProduct(productQueryParam);

        Page<Product> res = new Page<>();
        res.setLimit(limit);
        res.setOffset(offset);
        res.setTotal(total);
        res.setResult(list);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId){
        Product product = productService.getProductById(productId);

        if(product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest){
        int productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updatedProduct(@PathVariable int productId,
                                                  @RequestBody @Valid ProductRequest productRequest){

        if(productService.getProductById(productId) != null){
            if(productService.updateProduct(productId, productRequest) == STATUS_SUCCESS){
                Product product = productService.getProductById(productId);
                return ResponseEntity.status(HttpStatus.OK).body(product);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int productId){

        productService.deleteProductById(productId);

        if(productService.getProductById(productId) == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
