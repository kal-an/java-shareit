package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import javax.validation.Valid;
import java.util.Collection;
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
    public BookingDto addBooking(@Valid @RequestBody BookingShortDto bookingDto,
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
    public Collection<BookingDto> getAllUserBookings(@RequestParam(defaultValue = "ALL")
                                                                 String state,
                                                     @RequestHeader(X_HEADER_USER) long userId) {
        return bookingService.getAllUserBookings(state, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllOwnerBookings(@RequestParam(defaultValue = "ALL")
                                                                  String state,
                                                @RequestHeader(X_HEADER_USER) long userId) {
        return bookingService.getAllOwnerBookings(state, userId);
    }
}
