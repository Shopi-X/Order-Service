package com.shopix.oder.service;

import com.shopix.oder.dto.PlaceOderRequest;
import com.shopix.oder.entity.Order;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order create(Order order);
    Optional<Order> findById(String id);
    List<Order> findAll();
    Order update(String id, Order order);
    void deleteById(String id);

    Order placeOder(PlaceOderRequest order);
}
