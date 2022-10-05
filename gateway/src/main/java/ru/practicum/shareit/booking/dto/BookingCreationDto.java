package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreationDto {

    @NotNull(message = "Start date should not be null")
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull(message = "End date should not be null")
    @Future
    private LocalDateTime end;

    @NotNull(message = "Item ID should not be null")
    private Long itemId;

}
