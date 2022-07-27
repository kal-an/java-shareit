package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
public class UserDto {

    private Long id;

    @NotEmpty(message = "Name should not be empty")
    @Size(max = 255, message = "Name should less 255 characters")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email should not be empty")
    @Size(max = 512, message = "Email should less 512 characters")
    private String email;

    public UserDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
