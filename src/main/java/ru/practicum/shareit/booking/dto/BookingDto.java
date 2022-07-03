package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDate;

@Setter
@Getter
public class BookingDto {

    private Long id;
    private LocalDate start;
    private LocalDate end;
    private ItemDto item;
    private User booker;
    private Status status;

    public BookingDto(LocalDate start, LocalDate end, Long userId, Status status) {
    }
}
