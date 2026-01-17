package com.shopix.oder.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopix.oder.dto.OrderUpdateRequest;
import com.shopix.oder.grpc.UserServiceGrpcClient;
import com.shopix.oder.kafka.KafkaProducerService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
@RequiredArgsConstructor
public class StripeWebhookController {
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        // Only care about this event
        if ("checkout.session.completed".equals(event.getType())) {

            try {
                JsonNode root = objectMapper.readTree(payload);
                JsonNode sessionNode = root.path("data").path("object");

                String stripeSessionId = sessionNode.path("id").asText();
                String orderId = sessionNode.path("metadata").path("orderId").asText();

                System.out.println("======================================");
                System.out.println("Stripe Session ID: " + stripeSessionId);
                System.out.println("Order ID: " + orderId);
                System.out.println("======================================");

                kafkaProducerService.sendOrderUpdate(new OrderUpdateRequest(orderId, stripeSessionId));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Ignoring Stripe event: " + event.getType());
        }

        return ResponseEntity.ok("OK");
    }
}
