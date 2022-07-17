package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BookingDto {

    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private Long userId;
    private String status;

    public BookingDto(LocalDate start,
                      LocalDate end,
                      String name,
                      Long userId,
                      String status) {
        this.start = start;
        this.end = end;
        this.item.name = name;
        this.userId = userId;
        this.status = status;
    }

    private static class Item {
        private long id;
        private String name;
    }
}
