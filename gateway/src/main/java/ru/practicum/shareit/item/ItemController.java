package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto itemDto,
                                          @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                         @RequestHeader(X_HEADER_USER) long userId,
                         @PathVariable long itemId) {
        log.info("Updating items {}, userId={}", itemDto, userId);
        return itemClient.updateItem(itemId, userId, itemDto);

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                       @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getItem(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnerItems(
                @RequestParam(value = "from", defaultValue = "0",
                        required = false) @PositiveOrZero int fromPage,
                @RequestParam(defaultValue = "10", required = false) @Positive int size,
                @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Get owner items, userId={}", userId);
        return itemClient.getItems(userId, fromPage, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsForBooking(
                @RequestParam(value = "from", defaultValue = "0",
                        required = false) @PositiveOrZero int fromPage,
                @RequestParam(defaultValue = "10", required = false) @Positive int size,
                @RequestHeader(X_HEADER_USER) long userId,
                @RequestParam String text) {
        return itemClient.searchItems(fromPage, size, userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader(X_HEADER_USER) long userId,
                                                  @PathVariable long itemId,
                                                  @Valid @RequestBody CommentCreationDto text) {
        return itemClient.addComment(userId, itemId, text);
    }

    @GetMapping("/{itemId}/comment")
    public ResponseEntity<Object> searchCommentsForItem(@PathVariable long itemId,
                                                  @RequestHeader(X_HEADER_USER) long userId,
                                                  @RequestParam String text) {
        return itemClient.searchComments(itemId, userId, text);
    }
}
