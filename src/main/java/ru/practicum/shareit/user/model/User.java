package ru.practicum.shareit.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public class User {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    private String email;
}
