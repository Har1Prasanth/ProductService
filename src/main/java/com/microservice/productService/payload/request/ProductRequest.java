package com.microservice.productService.payload.request;

import lombok.*;

//@Builder
@Data
@NoArgsConstructor
public class ProductRequest {

    private String name;
    private long price;
    private long quantity;

}
