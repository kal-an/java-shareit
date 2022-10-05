package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@Validated
@Slf4j
@RequiredArgsConstructor
public class ItemRequestController {

    private final RequestClient requestClient;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@Valid @RequestBody ItemRequestDto requestDto,
                                             @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Creating request {}, userId={}", requestDto, userId);
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnRequests(@RequestHeader(X_HEADER_USER)
                                                                      long userId) {
        log.info("Get own requests, userId={}", userId);
        return requestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOtherRequests(
                @RequestParam(defaultValue = "0", value = "from",
                        required = false) @PositiveOrZero int fromPage,
                @RequestParam(defaultValue = "10", required = false) @Positive int size,
                @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Get all requests, userId={}", userId);
        return requestClient.getRequests(userId, fromPage, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@PathVariable long requestId,
                                                 @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Get request {}, userId={}", requestId, userId);
        return requestClient.getRequest(requestId, userId);
    }
}
