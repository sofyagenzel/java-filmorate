package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
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
        if (user.getName() == null || user.getName().isEmpty()) {
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
        if (userStorage.getUserById(id) != null) {
            return userStorage.getUserById(id);
        } else {
            throw new ObjectNotFoundException("Запись не найдена");
        }
    }

    public void removeUserById(int id) {
        if (userStorage.getUserById(id) != null) {
            userStorage.removeUserById(id);
        } else {
            throw new ObjectNotFoundException("Запись не удалена");
        }
    }

    public void addNewFriend(int id, int friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            String status;
            if (getListFriend(friendId).contains(getUserById(id))) {
                status = "подтвержден";
            } else {
                status = "не подтвержден";
            }
            userStorage.addNewFriend(id, friendId, status);
        }
    }

    public void removeFriend(int id, int friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            userStorage.removeFriend(id, friendId);
        }
    }

    public List<User> getListFriend(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            return userStorage.getListFriend(id);
        }
    }

    public List<User> getCommonFriends(int id, int secondId) {
        if ((userStorage.getUserById(id) == null || userStorage.getUserById(secondId) == null)) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            return userStorage.getCommonFriends(id, secondId);
        }
    }
}