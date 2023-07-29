package com.microservice.productService.service;

import com.microservice.productService.entity.Product;
import com.microservice.productService.entity.UserInfo;
import com.microservice.productService.exception.ProductServiceCustomException;
import com.microservice.productService.payload.request.ProductRequest;
import com.microservice.productService.payload.response.ProductResponse;
import com.microservice.productService.repository.ProductRepository;
import com.microservice.productService.repository.UserInforRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final UserInforRepository userInforRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("ProductServiceImpl | addProduct is called");

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        product = productRepository.save(product);

        log.info("ProductServiceImpl | addProduct | Product Created");
        log.info("ProductServiceImpl | addProduct | Product Id : " + product.getProductId());

        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("ProductServiceImpl | getProductById is called");
        log.info("ProductServiceImpl | getProductById | Get the product for productId: {}", productId);

        Product product=
                productRepository.findById(productId)
                        .orElseThrow(
                                ()->new ProductServiceCustomException("Product with given Id not found","404")
                        );

        ProductResponse productResponse=new ProductResponse();

        BeanUtils.copyProperties(product,productResponse);
        log.info("ProductServiceImpl | getProductById | productResponse :" + productResponse.toString());



        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {

        log.info("Reduce Quantity {} for Id: {}", quantity,productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given Id not found",
                        "PRODUCT_NOT_FOUND"
                ));

        if(product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product does not have sufficient Quantity",
                    "INSUFFICIENT_QUANTITY"
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated Successfully");

    }

    @Override
    public void deleteProductById(long productId) {
        log.info("Product id: {}", productId);

        if (!productRepository.existsById(productId)) {
            log.info("Im in this loop {}", !productRepository.existsById(productId));
            throw new ProductServiceCustomException(
                    "Product with given with Id: " + productId + " not found:",
                    "PRODUCT_NOT_FOUND");
        }
        log.info("Deleting Product with id: {}", productId);
        productRepository.deleteById(productId);

    }

    @Override
    public String addUser(UserInfo userInfo){

        userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        userInforRepository.save(userInfo);
        return "User added into the DB";
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Calling getAllProducts()");
        List<Product> products= productRepository.findAll();
        List<ProductResponse> productResponses=new ArrayList<>();
        products.stream()
                .forEach(product -> {
                    ProductResponse pr=new ProductResponse();
                    BeanUtils.copyProperties(product,pr);
                    productResponses.add(pr);
                });

        return productResponses;
    }
}
