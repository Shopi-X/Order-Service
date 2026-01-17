package com.shopix.oder.service;

import com.shopix.oder.entity.OrderDetail;

import java.util.List;
import java.util.Optional;

public interface OrderDetailService {
    OrderDetail create(OrderDetail orderDetail);
    Optional<OrderDetail> findById(String id);
    List<OrderDetail> findAll();
    OrderDetail update(String id, OrderDetail orderDetail);
    void deleteById(String id);
}
