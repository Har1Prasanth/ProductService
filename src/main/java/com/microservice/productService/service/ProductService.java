package com.microservice.productService.service;

import com.microservice.productService.entity.Product;
import com.microservice.productService.entity.UserInfo;
import com.microservice.productService.payload.request.ProductRequest;
import com.microservice.productService.payload.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId,long quantity);

    public void deleteProductById(long productId);

    String addUser(UserInfo userInfo);

    List<ProductResponse> getAllProducts();
}
