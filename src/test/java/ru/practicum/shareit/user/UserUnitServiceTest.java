package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class UserUnitServiceTest {

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Create user exception")
    void testCreateUserException() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@email.com");
        Mockito.when(userService.createUser(userDto))
                .thenThrow(new InvalidEntityException("User ID should be empty"));

        final InvalidEntityException exception = Assertions.assertThrows(InvalidEntityException.class,
                () -> userService.createUser(userDto));
        Assertions.assertEquals("User ID should be empty", exception.getMessage());
    }

    @Test
    @DisplayName("Find user exception")
    void testFindUserNotFound() {
        Mockito.when(userService.getUserById(anyLong()))
                .thenThrow(new UserNotFoundException("User with ID 2 not found"));

        final UserNotFoundException exception = Assertions.assertThrows(UserNotFoundException.class,
                () -> userService.getUserById(2L));
        Assertions.assertEquals("User with ID 2 not found", exception.getMessage());
    }

    @Test
    @DisplayName("Update name, email")
    void testUpdateUser() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("Scott");
        userDto.setEmail("scott@email.com");

        UserDto oldUser = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setEmail("john@email.com");
        Mockito.when(userService.updateUser(oldUser, 1L))
                .thenReturn(userDto);
        Mockito.when(userService.getUserById(1L))
                .thenReturn(userDto);

        Assertions.assertEquals(userDto.getName(), userService.getUserById(1L).getName());
        Assertions.assertEquals(userDto.getEmail(), userService.getUserById(1L).getEmail());
    }
}
