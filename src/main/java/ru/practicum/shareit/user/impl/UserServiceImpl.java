package ru.practicum.shareit.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictEntityException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        final User user = UserMapper.toUser(userDto);
        for (User u : userRepository.getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.error("This email {} already exist", userDto.getEmail());
                throw new ConflictEntityException("This email already exist");
            }
        }
        return UserMapper.toUserDto(userRepository.createUser(user).orElseThrow(() ->
                new UserNotFoundException(user.toString())));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        getUserById(userId);
        final User user = UserMapper.toUser(userDto);
        for (User u : getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())
                    && !u.getId().equals(userId)) {
                log.error("This email {} already exist", user.getEmail());
                throw new ConflictEntityException("This email already exist");
            }
        }
        return UserMapper.toUserDto(userRepository.updateUser(user, userId).orElseThrow(() ->
                new UserNotFoundException(user.toString())));
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(long id) {
        return userRepository.getUserById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID %d not found", id)));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }
}
