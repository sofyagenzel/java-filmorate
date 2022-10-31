package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Валидация пройдена.");
        return userService.create(user);
    }


    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Валидация пройдена.");
        userService.update(user);
        log.info("Данные пользователя обновлены.");
        return user;
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addNewFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{userId}")
    public void removeFriend(@PathVariable int id, @PathVariable int userId) {
        userService.removeFriend(id, userId);
    }

    @GetMapping(value = "/{id}/friends")
    public List<User> getListFriend(@PathVariable int id) {
        return userService.getListFriend(id);
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}