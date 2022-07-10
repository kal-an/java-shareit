package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserService {

    Optional<User> createUser(User user);

    Optional<User> updateUser(User user, Long userId);

    Collection<User> getAllUsers();

    User getUserById(long id);

    void deleteUser(long id);
}
