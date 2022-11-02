package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAll();

    void create(User user);

    void update(User user);

    User getUserById(int id) throws ObjectNotFoundException;

    void removeUserById(int id);

    void addNewFriend(int id, int friendId, String status);

    void removeFriend(int id, int friendId);

    List<User> getListFriend(int id);

    List<User> getCommonFriends(int id, int secondId);

}

