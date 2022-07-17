package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class UserDto {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    private String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
