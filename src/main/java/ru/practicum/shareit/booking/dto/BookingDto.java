package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class BookingDto {

    private Long id;

    @NotNull(message = "Start date should not be null")
    private LocalDateTime start;

    @NotNull(message = "End date should not be null")
    private LocalDateTime end;

    private Item item;

    private User booker;

    private Status status;

    @Getter
    @Setter
    public static class Item {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    public static class User {
        private Long id;
    }
}
