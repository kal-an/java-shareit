package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ItemRepositoryInMemoryImpl {

    private static final List<Item> items = new ArrayList<>();
    private static long itemId;

    public Optional<Item> addItem(Item item) {
        item.setId(++itemId);
        items.add(item);
        return Optional.of(item);
    }

    public Optional<Item> editItem(Item item, long itemId) {
        for (Item i : items) {
            if (i.getId().equals(itemId)) {
                if (item.getName() != null) {
                    i.setName(item.getName());
                }
                if (item.getDescription() != null) {
                    i.setDescription(item.getDescription());
                }
                if (item.getAvailable() != null) {
                    i.setAvailable(item.getAvailable());
                }
                return Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Optional<Item> getItemById(long id) {
        return items.stream().filter(item -> item.getId() == id).findAny();
    }

    public Collection<Item> getAllItems() {
        return items;
    }

    public List<Item> searchItemsForBooking(String text) {
        return items.stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }
}
