package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    UserDto createUser(UserDto userDto);

    UserDto updateUser(UserDto userDto, Long userId);

    Collection<UserDto> getAllUsers();

    UserDto getUserById(long id);

    void deleteUser(long id);
}
