package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictEntityException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> createUser(User user) {
        for (User u : userRepository.getAllUsers()) {
            if (u.getEmail().equals(user.getEmail())) {
                log.error("This email {} already exist", user.getEmail());
                throw new ConflictEntityException("This email already exist");
            }
        }
        return userRepository.createUser(user);
    }

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

    public Collection<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User getUserById(long id) {
        return userRepository.getUserById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("User with ID %d not found", id)));
    }

    public void deleteUser(long id) {
        userRepository.deleteUser(id);
    }
}
