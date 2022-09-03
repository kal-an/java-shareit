package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingCreationDto bookingDto,
                                             @RequestHeader(X_HEADER_USER) long bookerId) {
        log.info("Creating booking {}, userId={}", bookingDto, bookerId);
        return bookingClient.addBooking(bookerId, bookingDto);
    }

    @PatchMapping("{bookingId}")
    public ResponseEntity<Object> updateBooking(@RequestHeader(X_HEADER_USER) long ownerId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        log.info("Updating booking {}, userId={}, approved={}", bookingId, ownerId, approved);
        return bookingClient.updateBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@PathVariable long bookingId,
                                     @RequestHeader(X_HEADER_USER) long userId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUserBookings(
                @RequestParam(defaultValue = "ALL") String state,
                @RequestHeader(X_HEADER_USER) long userId,
                @RequestParam(value = "from", defaultValue = "0",
                        required = false) @PositiveOrZero int fromPage,
                @RequestParam(defaultValue = "10", required = false) @Positive int size) {
        log.info("Get user bookings, userId={}", userId);
        return bookingClient.getUserBookings(userId, state, fromPage, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerBookings(
                @RequestParam(defaultValue = "ALL") String state,
                @RequestHeader(X_HEADER_USER) long userId,
                @RequestParam(value = "from", defaultValue = "0",
                        required = false) @PositiveOrZero int fromPage,
                @RequestParam(defaultValue = "10", required = false) @Positive int size) {
        log.info("Get owner bookings, userId={}", userId);
        return bookingClient.getOwnerBookings(userId, state, fromPage, size);
    }
}
