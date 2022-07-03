package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Booking {

    private Long id;

    @NotNull(message = "Start date should not be null")
    private LocalDate start;

    @NotNull(message = "End date should not be null")
    private LocalDate end;

    @NotNull(message = "Item should not be null")
    private Item item;

    @NotNull(message = "Booker should not be null")
    private User booker;

    @NotNull(message = "Status should not be null")
    private Status status;
}
