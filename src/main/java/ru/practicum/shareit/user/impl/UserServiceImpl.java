package ru.practicum.shareit.user.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictEntityException;
import ru.practicum.shareit.user.UserNotFoundException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> createUser(User user) {
        for (User u : userRepository.getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.error("This email {} already exist", user.getEmail());
                throw new ConflictEntityException("This email already exist");
            }
        }
        return userRepository.createUser(user);
    }

    @Override
    public Optional<User> updateUser(User user, Long userId) {
        getUserById(userId);
        for (User u : getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())
                    && !u.getId().equals(userId)) {
                log.error("This email {} already exist", user.getEmail());
                throw new ConflictEntityException("This email already exist");
            }
        }

        return userRepository.updateUser(user, userId);
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
