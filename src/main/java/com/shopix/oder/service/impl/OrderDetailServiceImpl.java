package com.shopix.oder.service.impl;

import com.shopix.oder.entity.OrderDetail;
import com.shopix.oder.repositorry.OrderDetailRepository;
import com.shopix.oder.service.OrderDetailService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public OrderDetail create(OrderDetail orderDetail) {
        orderDetail.setId(null);
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public Optional<OrderDetail> findById(String id) {
        return orderDetailRepository.findById(id);
    }

    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail update(String id, OrderDetail orderDetail) {
        return orderDetailRepository.findById(id)
                .map(existing -> {
                    existing.setQuantity(orderDetail.getQuantity());
                    existing.setOrder(orderDetail.getOrder());
                    existing.setItem(orderDetail.getItem());
                    return orderDetailRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("OrderDetail not found: " + id));
    }

    @Override
    public void deleteById(String id) {
        orderDetailRepository.deleteById(id);
    }
}
