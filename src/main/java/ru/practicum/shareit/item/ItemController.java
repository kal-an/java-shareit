package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.Collection;

@RestController
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
        return service.addItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestBody ItemDto itemDto,
                         @RequestHeader("X-Sharer-User-Id") long userId,
                         @PathVariable long itemId) {
        return service.editItem(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return service.getItemById(itemId);
    }

    @GetMapping
    public Collection<ItemDto> getAllOwnerItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getAllOwnerItems(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItemsForBooking(@RequestParam String text) {
        return service.searchItemsForBooking(text);
    }
}
