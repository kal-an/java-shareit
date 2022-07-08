package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserService userService;

    @Autowired
    public ItemServiceImpl(ItemRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Item addItem(Item item, long userId) {
        item.setOwner(userService.getUserById(userId));
        return repository.addItem(item).orElseThrow();
    }

    @Override
    public Item editItem(Item item, long itemId, long userId) {
        final Item foundedItem = getItemById(itemId);
        if (!foundedItem.getOwner().getId().equals(userId)) {
            throw new UserNotFoundException("UserID should be item owner");
        }
        return repository.editItem(item, itemId).orElseThrow();
    }

    @Override
    public Item getItemById(long id) {
        return repository.getItemById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", id)));
    }

    private Collection<Item> getAllItems() {
        return repository.getAllItems();
    }

    @Override
    public List<Item> getAllOwnerItems(long userId) {
        userService.getUserById(userId);
        return getAllItems()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemsForBooking(String text) {
        return repository.searchItemsForBooking(text);
    }
}
