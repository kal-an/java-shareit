package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

public interface BookingService {

    BookingDto addBooking(BookingCreationDto bookingDto, long userId);

    BookingDto updateBooking(long bookingId, long userId, boolean approved);

    BookingDto getBookingById(long id, long userId);

    List<BookingDto> getAllUserBookings(@PositiveOrZero int fromPage,
                                        @Positive int size, String state, long userId);

    List<BookingDto> getAllOwnerBookings(@PositiveOrZero int fromPage,
                                         @Positive int size, String state, long userId);

}
