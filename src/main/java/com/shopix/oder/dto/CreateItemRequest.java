package com.shopix.oder.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateItemRequest {
    private String itemName;
    private String itemDescription;
    private double itemPrice;
    private int availableQuantity;
}
