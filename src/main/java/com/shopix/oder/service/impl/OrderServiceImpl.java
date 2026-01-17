package com.shopix.oder.service.impl;

import com.shopix.oder.dto.OrderItemRequest;
import com.shopix.oder.dto.PlaceOderRequest;
import com.shopix.oder.dto.StripeSessionView;
import com.shopix.oder.dto.UserView;
import com.shopix.oder.entity.Item;
import com.shopix.oder.entity.Order;
import com.shopix.oder.entity.OrderDetail;
import com.shopix.oder.enums.OrderStatus;
import com.shopix.oder.grpc.UserServiceGrpcClient;
import com.shopix.oder.repositorry.ItemRepository;
import com.shopix.oder.repositorry.OrderRepository;
import com.shopix.oder.service.OrderService;
import com.shopix.oder.service.StripeCheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final UserServiceGrpcClient  userServiceGrpcClient;
    private final StripeCheckoutService stripeCheckoutService;

    @Override
    public Order create(Order order) {
        order.setId(null);
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order update(String id, Order order) {
        return orderRepository.findById(id)
                .map(existing -> {
                    existing.setOrderDate(order.getOrderDate());
                    existing.setOrderStatus(order.getOrderStatus());
                    existing.setItems(order.getItems());
                    return orderRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    public void deleteById(String id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Order placeOder(PlaceOderRequest order) {
        long totalPrice = 0;

        if (order == null || order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            throw new RuntimeException("Order is empty");
        }

        Order orderEntity = new Order();
        orderEntity.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        for (OrderItemRequest reqItem : order.getOrderItems()) {

            Item dbItem = itemRepository.findById(reqItem.getItemId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (reqItem.getQuantity() > dbItem.getAvailableQuantity()) {
                throw new RuntimeException("Quantity exceeds available");
            }

            // reduce stock
            dbItem.setAvailableQuantity(dbItem.getAvailableQuantity() - reqItem.getQuantity());
            itemRepository.save(dbItem);

            // create order detail
            OrderDetail detail = new OrderDetail();
            detail.setItem(dbItem);
            detail.setQuantity(reqItem.getQuantity());

            totalPrice = (long) ((long) detail.getQuantity() * dbItem.getItemPrice());

            // 🔥 THIS LINE FIXES YOUR CRASH
            orderEntity.addItem(detail);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        UserView userByKeycloakId = userServiceGrpcClient.getUserByKeycloakId(jwt.getSubject());
        if (userByKeycloakId.getId() == null) {
            throw new RuntimeException("User not found");
        }
        orderEntity.setCustomerId(userByKeycloakId.getId());
        orderEntity.setTotalAmount(totalPrice);

        Order saveOrder = orderRepository.save(orderEntity);

        StripeSessionView checkout = stripeCheckoutService.createCheckoutUrl(saveOrder);


        saveOrder.setStripeSessionId(checkout.getSessionId());
        saveOrder.setStripeSessionUrl(checkout.getUrl());
        // 🔥 Save ONLY parent
        return orderRepository.save(saveOrder);
    }
}
