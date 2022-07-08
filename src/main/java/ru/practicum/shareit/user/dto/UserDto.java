package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private String email;

    public UserDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
