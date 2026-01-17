package com.shopix.oder.service.impl;

import com.shopix.oder.dto.CreateItemRequest;
import com.shopix.oder.entity.Item;
import com.shopix.oder.repositorry.ItemRepository;
import com.shopix.oder.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Item create(CreateItemRequest item) {
        Item mapped = modelMapper.map(item, Item.class);
        mapped.setId(null); // ensure new entity
        return itemRepository.save(mapped);
    }

    @Override
    public Optional<Item> findById(String id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item update(String id, Item item) {
        return itemRepository.findById(id)
                .map(existing -> {
                    existing.setItemName(item.getItemName());
                    existing.setItemDescription(item.getItemDescription());
                    existing.setItemPrice(item.getItemPrice());
                    existing.setAvailableQuantity(item.getAvailableQuantity());
                    return itemRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Item not found: " + id));
    }

    @Override
    public void deleteById(String id) {
        itemRepository.deleteById(id);
    }
}
