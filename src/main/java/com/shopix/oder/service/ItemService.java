package com.shopix.oder.service;

import com.shopix.oder.dto.CreateItemRequest;
import com.shopix.oder.entity.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    Item create(CreateItemRequest item);
    Optional<Item> findById(String id);
    List<Item> findAll();
    Item update(String id, Item item);
    void deleteById(String id);
}
