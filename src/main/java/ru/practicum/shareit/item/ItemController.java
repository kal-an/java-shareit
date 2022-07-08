package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;

@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
                        @RequestHeader("X-Sharer-User-Id") long userId) {
        if (itemDto.getId() != null) {
            log.error("Item ID should be empty {}", itemDto);
            throw new InvalidEntityException("Item ID should be empty");
        }
        final Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(service.addItem(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") long userId,
                         @PathVariable long itemId) {
        final Item item = ItemMapper.toItem(itemDto);
        return ItemMapper.toItemDto(service.editItem(item, itemId, userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return ItemMapper.toItemDto(service.getItemById(itemId));
    }

    @GetMapping
    public Collection<Item> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    Collection<Item> searchItemsForBooking(@RequestParam String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        return service.searchItemsForBooking(text);
    }
}
