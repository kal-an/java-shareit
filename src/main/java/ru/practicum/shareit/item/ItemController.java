package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoExtended;

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
    public ItemDtoExtended getItemById(@PathVariable long itemId,
                                       @RequestHeader(X_HEADER_USER) long userId) {
        return service.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoExtended> getAllOwnerItems(
                @RequestParam(value = "from", defaultValue = "0", required = false) int fromPage,
                @RequestParam(defaultValue = "10", required = false) int size,
                @RequestHeader(X_HEADER_USER) long userId) {
        return service.getAllOwnerItems(fromPage, size, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItemsForBooking(
                @RequestParam(value = "from", defaultValue = "0", required = false) int fromPage,
                @RequestParam(defaultValue = "10", required = false) int size,
                @RequestParam String text) {
        return service.searchItemsForBooking(fromPage, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader(X_HEADER_USER) long userId,
                                                  @PathVariable long itemId,
                                                  @Valid @RequestBody CommentCreationDto text) {
        return service.addComment(userId, itemId, text);
    }

    @GetMapping("/{itemId}/comment")
    public List<CommentDto> searchCommentsForItem(@PathVariable long itemId,
                                                  @RequestParam String text) {
        return service.searchCommentsForItem(itemId, text);
    }
}
