package ru.yandex.practicum.filmorate.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.StatusFriendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user) {
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        userStorage.create(user);
        return user;
    }

    public User update(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        userStorage.update(user);
        return getUserById(user.getId());
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User getUserById(int id) {
            return userStorage.getUserById(id);
    }

    public void removeUserById(int id) {
        userStorage.getUserById(id);
        userStorage.removeUserById(id);
    }

    public void addNewFriend(int id, int friendId) {
        userStorage.getUserById(id);
        userStorage.getUserById(friendId);
        String status;
        if (getListFriend(friendId).contains(getUserById(id))) {
            status = String.valueOf(StatusFriendship.VALIDATED);
        } else {
            status = String.valueOf(StatusFriendship.NOT_CONFIRMED);
        }
        userStorage.addNewFriend(id, friendId, status);
    }

    public void removeFriend(int id, int friendId) {
            userStorage.removeFriend(id, friendId);
    }

    public List<User> getListFriend(int id) {
        userStorage.getUserById(id);
        return userStorage.getListFriend(id);
    }

    public List<User> getCommonFriends(int id, int secondId) {
        userStorage.getUserById(id);
        userStorage.getUserById(secondId);
        return userStorage.getCommonFriends(id, secondId);
    }
}