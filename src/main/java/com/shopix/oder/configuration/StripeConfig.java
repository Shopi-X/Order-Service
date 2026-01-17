package com.shopix.oder.configuration;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalStateException("STRIPE_SECRET_KEY is missing. Set env var STRIPE_SECRET_KEY.");
        }
        Stripe.apiKey = secretKey;
    }
}
