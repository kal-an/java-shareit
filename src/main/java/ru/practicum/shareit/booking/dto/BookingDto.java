package ru.practicum.shareit.booking.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
public class BookingDto {

    private Long id;

    @NotNull(message = "Start date should not be null")
    private LocalDateTime start;

    @NotNull(message = "End date should not be null")
    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private Status status;

    public BookingDto(Long id,
                      LocalDateTime start,
                      LocalDateTime end,
                      Status status) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.status = status;
    }
}
