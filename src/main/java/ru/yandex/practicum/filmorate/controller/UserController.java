package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final HashMap<Integer, User> users = new HashMap();
    private final ArrayList<User> usersArray = new ArrayList<>();
    int idUser;

    @GetMapping
    public ArrayList<User> findAll() {
        usersArray.clear();
        for (int id : users.keySet()) {
            usersArray.add(users.get(id));
        }
        return usersArray;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Валидация пройдена.");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        idUser = idUser + 1;
        user.setId(idUser);
        users.put(idUser, user);
        log.info("Добавлен пользователь.");
        return user;
    }

    @PutMapping
    public User upload(@Valid @RequestBody User user) {
        log.info("Валидация пройдена.");
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Данные пользователя обновлены.");
        } else {
            throw new ValidationException("Запись не добавлена");
        }
        return user;
    }

}






