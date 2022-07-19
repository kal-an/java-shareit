package ru.practicum.shareit.user.impl;

import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class UserRepositoryInMemoryImpl {

    private static final List<User> users = new ArrayList<>();
    private static long userId;

    public Optional<User> createUser(User user) {
        user.setId(++userId);
        users.add(user);
        return Optional.of(user);
    }

    public Collection<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(long id) {
        return users.stream().filter(user -> user.getId() == id).findAny();
    }

    public Optional<User> updateUser(User user, long userId) {
        for (User u : users) {
            if (u.getId().equals(userId)) {
                if (user.getName() != null) {
                    u.setName(user.getName());
                }
                if (user.getEmail() != null) {
                    u.setEmail(user.getEmail());
                }
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }

    public void deleteUser(long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
