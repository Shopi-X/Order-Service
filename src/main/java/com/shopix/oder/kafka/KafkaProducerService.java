package com.shopix.oder.kafka;

import com.shopix.oder.dto.OrderUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, OrderUpdateRequest> kafkaTemplate;

    public void sendOrderUpdate(OrderUpdateRequest request) {
        kafkaTemplate.send("order.update.topic", request.getOrderId(), request);
        log.info("Order update sent to topic: {}", request.getOrderId());
    }
}
