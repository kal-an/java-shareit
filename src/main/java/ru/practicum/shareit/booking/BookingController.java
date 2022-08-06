package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;
    private static final String X_HEADER_USER = "X-Sharer-User-Id";

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingCreationDto bookingDto,
                                 @RequestHeader(X_HEADER_USER) long bookerId) {
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("{bookingId}")
    public BookingDto updateBooking(@RequestHeader(X_HEADER_USER) long ownerId,
                                    @PathVariable long bookingId,
                                    @RequestParam boolean approved) {
        return bookingService.updateBooking(bookingId, ownerId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId,
                                     @RequestHeader(X_HEADER_USER) long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllUserBookings(
                @RequestParam(defaultValue = "ALL") String state,
                @RequestHeader(X_HEADER_USER) long userId,
                @RequestParam(value = "from", defaultValue = "0", required = false) int fromPage,
                @RequestParam(defaultValue = "10", required = false) int size) {
        return bookingService.getAllUserBookings(fromPage, size, state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(
                @RequestParam(defaultValue = "ALL") String state,
                @RequestHeader(X_HEADER_USER) long userId,
                @RequestParam(value = "from", defaultValue = "0", required = false) int fromPage,
                @RequestParam(defaultValue = "10", required = false) int size) {
        return bookingService.getAllOwnerBookings(fromPage, size, state, userId);
    }
}
