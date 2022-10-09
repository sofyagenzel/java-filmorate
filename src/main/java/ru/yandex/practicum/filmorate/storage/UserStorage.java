package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

public interface UserStorage {
    ArrayList<User> findAll();

    void create(User user);

    void update(User user);

    User getUserById(int id);

    void removeUserById(int id);
}