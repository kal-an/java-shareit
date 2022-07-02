package ru.practicum.shareit.user;

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
    private String email;
}
