package com.example.products_service.services;

import com.example.products_service.model.dtos.ProductRequest;
import com.example.products_service.model.dtos.ProductResponse;
import com.example.products_service.model.entities.Product;
import com.example.products_service.respositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest) {
        log.info("Adding a product");

        var product = Product.builder()
                .sku(productRequest.getSku())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .status(productRequest.getStatus())
                .build();

        productRepository.save(product);

        log.info("Product added successfully {}", product);
    }

    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");

        return productRepository.findAll().stream()
                .map(this::toProductResponse)
                .toList();
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .status(product.getStatus())
                .build();
    }
}
