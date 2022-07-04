package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Setter
@Getter
public class BookingDto {

    private Long id;
    private LocalDate start;
    private LocalDate end;
    private Item item;
    private Long userId;
    private Status status;

    public BookingDto(LocalDate start,
                      LocalDate end,
                      String name,
                      Long userId,
                      Status status) {
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
