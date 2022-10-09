package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public ArrayList<User> findAll() {
        return userStorage.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Валидация пройдена.");
        userStorage.create(user);
        log.info("Добавлен пользователь.");
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Валидация пройдена.");
        userStorage.update(user);
        log.info("Данные пользователя обновлены.");
        return user;
    }

    @GetMapping(value = "/{id}")
    public User getFilmById(@PathVariable int id) {
        return userStorage.getUserById(id);
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public User addNewFriend(@PathVariable int id, @PathVariable int friendId) {
        return userService.addNewFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{userId}")
    public User removeFriend(@PathVariable int id, @PathVariable int userId) {
        return userService.removeFriend(id, userId);
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