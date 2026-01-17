package com.shopix.oder.controller;

import com.shopix.oder.dto.PlaceOderRequest;
import com.shopix.oder.dto.UserView;
import com.shopix.oder.entity.Order;
import com.shopix.oder.grpc.UserServiceGrpcClient;
import com.shopix.oder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;
    private final UserServiceGrpcClient userServiceGrpcClient;

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody PlaceOderRequest order){
        return ResponseEntity.ok().body(orderService.placeOder(order));
    }

    @GetMapping("test/user/{id}")
    public ResponseEntity<UserView> getOrders(@PathVariable String id){
        return ResponseEntity.ok().body(userServiceGrpcClient.getUserByKeycloakId(id));
    }

    @GetMapping("test/get-user")
    public String getCurrentKeycloakUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getSubject();
    }
}
