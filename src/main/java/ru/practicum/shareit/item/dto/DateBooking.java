package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@ToString
public class DateBooking {

    @NotNull(message = "ID should not be empty")
    private Long id;

    @NotNull(message = "Booker ID should not be empty")
    private Long bookerId;

    @NotNull(message = "Start date should not be empty")
    private LocalDateTime start;

    @NotNull(message = "End date should not be empty")
    private LocalDateTime end;
}
