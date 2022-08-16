package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> jsonBooking;
    @Autowired
    private JacksonTester<BookingCreationDto> jsonBookingCreation;

    @Test
    void testBookingDto() throws Exception {
        BookingDto.Item item = BookingDto.Item.builder()
                .id(1L)
                .name("table")
                .build();
        BookingDto.User user = BookingDto.User.builder()
                .id(1L)
                .build();
        BookingDto bookingDto = new BookingDto(
                1L,
                LocalDateTime.of(2022, 1, 1, 14, 0, 0),
                LocalDateTime.of(2022, 1, 1, 16, 0, 0),
                item,
                user,
                "WAITING");

        JsonContent<BookingDto> result = jsonBooking.write(bookingDto);
        System.out.println(result);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("table");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("WAITING");
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-01-01T14:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-01-01T16:00");
    }

    @Test
    void testBookingCreationDto() throws Exception {
        BookingCreationDto creationDto = new BookingCreationDto(
                LocalDateTime.of(2022, 1, 1, 14, 0, 0),
                LocalDateTime.of(2022, 1, 1, 16, 0, 0),
                1L);

        JsonContent<BookingCreationDto> result = jsonBookingCreation.write(creationDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2022-01-01T14:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2022-01-01T16:00");
    }

}
