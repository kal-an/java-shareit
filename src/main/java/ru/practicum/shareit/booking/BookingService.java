package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreationDto;

import java.util.List;

public interface BookingService {

    BookingDto addBooking(BookingCreationDto bookingDto, long userId);

    BookingDto updateBooking(long bookingId, long userId, boolean approved);

    BookingDto getBookingById(long id, long userId);

    List<BookingDto> getAllUserBookings(int fromPage, int size, String state, long userId);

    List<BookingDto> getAllOwnerBookings(int fromPage, int size, String state, long userId);

}
