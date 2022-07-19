package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ItemService service;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";

    @Autowired
    public ItemController(ItemService service) {
        this.service = service;
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto,
                        @RequestHeader(X_HEADER_USER) long userId) {
        return service.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestBody ItemDto itemDto,
                         @RequestHeader(X_HEADER_USER) long userId,
                         @PathVariable long itemId) {
        return service.editItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return service.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllOwnerItems(@RequestHeader(X_HEADER_USER) long userId) {
        return service.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsForBooking(@RequestParam String text) {
        return service.searchItemsForBooking(text);
    }
}
