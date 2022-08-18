package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        var item = BookingDto.Item.builder()
                .id(booking.getItem().getId())
                .name(booking.getItem().getName())
                .build();
        var user = BookingDto.User.builder()
                .id(booking.getBooker().getId())
                .build();
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus().toString())
                .item(item)
                .booker(user)
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }

    public static List<BookingDto> convertToListDto(List<Booking> bookings) {
        final List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            final BookingDto convertToDto = BookingMapper.toBookingDto(booking);
            bookingDtoList.add(convertToDto);
        }
        return bookingDtoList;
    }
}
