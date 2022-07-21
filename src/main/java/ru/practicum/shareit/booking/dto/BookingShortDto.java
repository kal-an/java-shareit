package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class BookingShortDto {

    @NotNull(message = "Start date should not be null")
    private LocalDateTime start;

    @NotNull(message = "End date should not be null")
    private LocalDateTime end;

    @NotNull(message = "Item ID should not be null")
    private Long itemId;

}
