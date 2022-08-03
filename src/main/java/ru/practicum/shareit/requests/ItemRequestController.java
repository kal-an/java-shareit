package ru.practicum.shareit.requests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoExtended;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService requestService;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";

    @Autowired
    public ItemRequestController(ItemRequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto requestDto,
                                     @RequestHeader(X_HEADER_USER) long userId) {
        return requestService.addRequest(requestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDtoExtended> getAllOwnRequests(@RequestHeader(X_HEADER_USER)
                                                                      long userId) {
        return requestService.getAllOwnRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoExtended> getAllOtherRequests(
                @RequestParam(value = "from", defaultValue = "0", required = false) int fromPage,
                @RequestParam(defaultValue = "10", required = false) int size,
                @RequestHeader(X_HEADER_USER) long userId) {
        return requestService.getAllOtherRequests(fromPage, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoExtended getRequestById(@PathVariable long requestId,
                                                 @RequestHeader(X_HEADER_USER) long userId) {
        return requestService.getRequestById(requestId, userId);
    }
}
