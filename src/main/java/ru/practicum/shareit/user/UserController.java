package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.InvalidEntityException;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getId() != null) {
            log.error("User ID should be empty {}", user);
            throw new InvalidEntityException("User ID should be empty");
        }
        return service.createUser(user).orElseThrow(() ->
                new UserNotFoundException(user.toString()));
    }

    @PatchMapping("/{id}")
    public User update(@RequestBody User user,
                       @PathVariable long id) {
        return service.updateUser(user, id).orElseThrow(() ->
                new UserNotFoundException(user.toString()));
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return service.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        service.deleteUser(id);
    }
}
