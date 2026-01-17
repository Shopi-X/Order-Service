package com.shopix.oder.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderItemRequest {
    private String itemId;
    private int Quantity;
}
