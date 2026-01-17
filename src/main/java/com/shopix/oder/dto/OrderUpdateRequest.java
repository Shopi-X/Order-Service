package com.shopix.oder.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class OrderUpdateRequest {
    private String orderId;
    private String sessionId;
}
