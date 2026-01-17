package com.shopix.oder.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PlaceOderRequest {
    private List<OrderItemRequest> orderItems;
}
