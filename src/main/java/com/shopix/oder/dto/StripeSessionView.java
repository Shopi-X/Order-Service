package com.shopix.oder.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class StripeSessionView {
    private String sessionId;
    private String url;
}
