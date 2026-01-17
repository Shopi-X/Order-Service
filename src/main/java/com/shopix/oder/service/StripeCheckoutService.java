package com.shopix.oder.service;

import com.shopix.oder.dto.StripeSessionView;
import com.shopix.oder.entity.Order;

public interface StripeCheckoutService {
    StripeSessionView createCheckoutUrl(Order order);
}
