package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class BookingDto {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Item item;

    private User booker;

    private String status;

    @Getter
    @Setter
    @Builder
    public static class Item {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @Builder
    public static class User {
        private Long id;
    }
}
