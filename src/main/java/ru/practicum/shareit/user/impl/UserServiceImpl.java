package ru.practicum.shareit.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictEntityException;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

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
        if (userDto.getId() != null) {
            log.error("User ID should be empty {}", user);
            throw new InvalidEntityException("User ID should be empty");
        }
        final User savedUserInDb = userRepository.save(user);
        log.info("User {} saved", savedUserInDb);
        return UserMapper.toUserDto(savedUserInDb);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long userId) {
        final User userInDb = userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID %d not found", userId)));
        if (userDto.getName() != null) userInDb.setName(userDto.getName());
        if (userDto.getEmail() != null) userInDb.setEmail(userDto.getEmail());
        final User updatedUserInDb = userRepository.save(userInDb);
        log.info("User {} updated", updatedUserInDb);
        return UserMapper.toUserDto(updatedUserInDb);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(long id) {
        final User user = userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID %d not found", id)));
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }
}
