package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {

    private static final List<User> users = new ArrayList<>();
    private static long userId;

    @Override
    public Optional<User> createUser(User user) {
        user.setId(++userId);
        users.add(user);
        return Optional.of(user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return users.stream().filter(user -> user.getId() == id).findAny();
    }

    @Override
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

    @Override
    public void deleteUser(long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
