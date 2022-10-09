package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addNewFriend(int id, int friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            User user = userStorage.getUserById(id);
            user.addFriend(friendId);
            userStorage.update(user);
            User friend = userStorage.getUserById(friendId);
            friend.addFriend(id);
            userStorage.update(friend);
            return user;
        }
    }

    public User removeFriend(int id, int friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            userStorage.getUserById(id).removeFriend(friendId);
            return userStorage.getUserById(id);
        }
    }

    public List<User> getListFriend(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            User user = userStorage.getUserById(id);
            List<User> userFriends = new ArrayList<>();
            for (int friendId : user.getFriends()) {
                userFriends.add(userStorage.getUserById(friendId));
            }
            return userFriends;
        }
    }

    public List<User> getCommonFriends(int id, int secondId) {
        if ((userStorage.getUserById(id) == null || userStorage.getUserById(secondId) == null)) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            List<User> commonFriends = new ArrayList<>();
            for (int firstUserId : userStorage.getUserById(id).getFriends()) {
                for (int secondUserId : userStorage.getUserById(secondId).getFriends()) {
                    if (Objects.equals(firstUserId, secondUserId)) {
                        commonFriends.add(userStorage.getUserById(firstUserId));
                    }
                }
            }
            return commonFriends;
        }
    }
}