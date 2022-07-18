package ru.practicum.shareit.item.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemNotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.Collections;
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
    public ItemDto addItem(ItemDto itemDto, long userId) {
        final Item item = ItemMapper.toItem(itemDto);
        userService.getUserById(userId);
        item.setOwner(userId);
        if (itemDto.getId() != null) {
            log.error("Item ID should be empty {}", item);
            throw new InvalidEntityException("Item ID should be empty");
        }
        final Item addedItem = repository.addItem(item).orElseThrow(() ->
                new ItemNotFoundException(item.toString()));
        return ItemMapper.toItemDto(addedItem);
    }

    @Override
    public ItemDto editItem(ItemDto itemDto, long itemId, long userId) {
        final Item foundedItem = repository.getItemById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", itemId)));
        userService.getUserById(userId);
        final Item item = ItemMapper.toItem(itemDto);
        if (!foundedItem.getOwner().equals(userId)) {
            log.error("UserID not equal item owner");
            throw new UserNotFoundException("UserID should be item owner");
        }
        final Item editedItem = repository.editItem(item, itemId).orElseThrow(() ->
                new ItemNotFoundException(item.toString()));
        return ItemMapper.toItemDto(editedItem);
    }

    @Override
    public ItemDto getItemById(long id) {
        final Item item = repository.getItemById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", id)));
        return ItemMapper.toItemDto(item);
    }

    private Collection<Item> getAllItems() {
        return repository.getAllItems();
    }

    @Override
    public List<ItemDto> getAllOwnerItems(long userId) {
        userService.getUserById(userId);
        return getAllItems()
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItemsForBooking(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return repository.searchItemsForBooking(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
