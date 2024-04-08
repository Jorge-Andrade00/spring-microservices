package com.example.inventory_service.model.dtos;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    private String sku;
    private Double price;
    private Long quantity;
}
