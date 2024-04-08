package com.example.orders_service.model.dtos;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    private String sku;
    private Double price;
    private Long quantity;
}
