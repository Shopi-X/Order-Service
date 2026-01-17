package com.shopix.oder.controller;

import com.shopix.oder.dto.CreateItemRequest;
import com.shopix.oder.dto.OrderUpdateRequest;
import com.shopix.oder.entity.Item;
import com.shopix.oder.kafka.KafkaProducerService;
import com.shopix.oder.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/item")
public class ItemController {
    private final ItemService itemService;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping()
    public ResponseEntity<String> createItem(@RequestBody CreateItemRequest item) {
        Item savedItem = itemService.create(item);
        return ResponseEntity.ok("Item created"+savedItem.getId());
    }

    @GetMapping()
    public ResponseEntity<List<Item>> getAllItems() {
        kafkaProducerService.sendOrderUpdate(new OrderUpdateRequest("6a8b1907-b0ce-40c0-99a6-8c61303751ec","indumawijesinghe"));
        return ResponseEntity.ok().body(itemService.findAll());
    }
}
