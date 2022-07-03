package ru.practicum.shareit.requests.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class ItemRequest {

    private Long id;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 200, message = "Description should less 200 characters")
    private String description;

    @NotNull(message = "User should not be null")
    private User requestor;

    @NotNull(message = "Created date should not be null")
    private LocalDateTime created;
}
