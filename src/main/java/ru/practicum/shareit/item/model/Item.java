package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class Item {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @NotEmpty(message = "Description should not be empty")
    @Size(max = 200, message = "Description should less 200 characters")
    private String description;

    @NotEmpty(message = "Status should not be empty")
    private Boolean available;

    @NotNull(message = "User should not be null")
    private User owner;

    private ItemRequest request;
}
