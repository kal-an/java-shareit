package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> createUser(User user);

    Collection<User> getAllUsers();

    Optional<User> getUserById(long id);

    Optional<User> updateUser(User user, long userId);

    void deleteUser(long id);
}
