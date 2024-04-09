package com.example.orders_service.services;

import com.example.orders_service.model.dtos.OrderItemRequest;
import com.example.orders_service.model.dtos.OrderItemResponse;
import com.example.orders_service.model.dtos.OrderRequest;
import com.example.orders_service.model.dtos.OrderResponse;
import com.example.orders_service.model.entities.Order;
import com.example.orders_service.model.entities.OrderItems;
import com.example.orders_service.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String createOrder(OrderRequest orderRequest) {

        if (!checkProductAvailability(orderRequest)) {
            return "Some products are not available!";
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItems(orderRequest.getOrderItems().stream()
                .map(orderItemRequest -> mapOrderItemsRequestToOrderItems(orderItemRequest, order))
                .toList());

        orderRepository.save(order);

        return "Order created successfully!";
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToOrderResponse).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber(), order.getOrderItems().stream()
                .map(this::mapToOrderItemResponse)
                .toList());
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItems orderItems) {
        return new OrderItemResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }

    private Boolean checkProductAvailability(OrderRequest orderRequest) {
        return webClientBuilder.build()
                .post()
                .uri("lb://inventory-service/api/inventory/in-stock")
                .bodyValue(orderRequest.getOrderItems())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }
    private OrderItems mapOrderItemsRequestToOrderItems(OrderItemRequest orderItemRequest, Order order) {
        return OrderItems.builder()
                .sku(orderItemRequest.getSku())
                .price(orderItemRequest.getPrice())
                .quantity(orderItemRequest.getQuantity())
                .order(order)
                .build();
    }
}
