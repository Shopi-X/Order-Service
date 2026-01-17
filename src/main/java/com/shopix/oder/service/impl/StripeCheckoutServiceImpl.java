package com.shopix.oder.service.impl;

import com.shopix.oder.dto.StripeSessionView;
import com.shopix.oder.entity.Order;
import com.shopix.oder.enums.OrderStatus;
import com.shopix.oder.repositorry.OrderRepository;
import com.shopix.oder.service.StripeCheckoutService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StripeCheckoutServiceImpl implements StripeCheckoutService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public StripeSessionView createCheckoutUrl(Order order) {



        try {
            // 3️⃣ Build Stripe session params
            SessionCreateParams params =
                    SessionCreateParams.builder()
                            .setMode(SessionCreateParams.Mode.PAYMENT)
                            .setSuccessUrl("http://localhost:3000/payment-success?orderId=" + order.getId())
                            .setCancelUrl("http://localhost:3000/payment-cancel?orderId=" + order.getId())
                            .putMetadata("orderId", order.getId())
                            .addLineItem(
                                    SessionCreateParams.LineItem.builder()
                                            .setQuantity(1L)
                                            .setPriceData(
                                                    SessionCreateParams.LineItem.PriceData.builder()
                                                            .setCurrency("usd") // or "lkr" if enabled in Stripe
                                                            .setUnitAmount(order.getTotalAmount())
                                                            .setProductData(
                                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                            .setName("Order #" + order.getId())
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build();

            Session session = Session.create(params);

            return new StripeSessionView(session.getId(),session.getUrl());

        } catch (StripeException e) {
            throw new RuntimeException("Stripe checkout session creation failed", e);
        }
    }
}
