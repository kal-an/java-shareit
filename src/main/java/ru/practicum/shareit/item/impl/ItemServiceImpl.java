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
        final Item saveItemInDb = repository.save(item);
        log.info("Item {} saved", saveItemInDb);
        return ItemMapper.toItemDto(saveItemInDb);
    }

    @Override
    public ItemDto editItem(ItemDto itemDto, long itemId, long userId) {
        final Item itemInDb = repository.findById(itemId).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", itemId)));
        userService.getUserById(userId);
        if (!itemInDb.getOwner().equals(userId)) {
            log.error("UserID not equal item owner");
            throw new UserNotFoundException("UserID should be item owner");
        }
        if (itemDto.getAvailable() != null) itemInDb.setAvailable(itemDto.getAvailable());
        if (itemDto.getName() != null) itemInDb.setName(itemDto.getName());
        if (itemDto.getDescription() != null) itemInDb.setDescription(itemDto.getDescription());
        final Item updateItemInDb = repository.save(itemInDb);
        log.info("Item {} updated", updateItemInDb);
        return ItemMapper.toItemDto(updateItemInDb);
    }

    @Override
    public ItemDto getItemById(long id) {
        final Item item = repository.findById(id).orElseThrow(() ->
                new ItemNotFoundException(String.format("Item with ID %d not found", id)));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getAllOwnerItems(long ownerId) {
        userService.getUserById(ownerId);
        return repository.findAllItemsByOwner(ownerId).stream()
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
