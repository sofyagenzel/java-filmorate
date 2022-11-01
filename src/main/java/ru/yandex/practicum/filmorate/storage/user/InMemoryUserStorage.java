package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap();
    private final List<User> usersArray = new ArrayList<>();
    private int idUser = 0;

    public List<User> findAll() {
        usersArray.clear();
        for (int id : users.keySet()) {
            usersArray.add(users.get(id));
        }
        return usersArray;
    }

    public void create(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        idUser = idUser + 1;
        user.setId(idUser);
        users.put(idUser, user);
    }

    public void update(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
        } else {
            throw new ObjectNotFoundException("Запись не добавлена");
        }
    }

    public User getUserById(int id) {
        if (users.get(id) != null) {
            return users.get(id);
        } else {
            throw new ObjectNotFoundException("Запись не добавлена");
        }
    }

    public void removeUserById(int id) {
        if (users.get(id) != null) {
            users.remove(id);
        } else {
            throw new ObjectNotFoundException("Запись не удалена");
        }
    }

    public void addNewFriend(int id, int friendId, String status) {
        if (getUserById(id) == null || getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            User user = getUserById(id);
            user.addFriend(friendId);
            update(user);
            User friend = getUserById(friendId);
            friend.addFriend(id);
            update(friend);
        }
    }

    public void removeFriend(int id, int friendId) {
        if (getUserById(id) == null || getUserById(friendId) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            getUserById(id).removeFriend(friendId);
        }
    }

    public List<User> getListFriend(int id) {
        if (getUserById(id) == null) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            User user = getUserById(id);
            List<User> userFriends = new ArrayList<>();
            for (int friendId : user.getFriends()) {
                userFriends.add(getUserById(friendId));
            }
            return userFriends;
        }
    }

    public List<User> getCommonFriends(int id, int secondId) {
        if ((getUserById(id) == null || getUserById(secondId) == null)) {
            throw new ObjectNotFoundException("Запись не найдена");
        } else {
            List<User> commonFriends = new ArrayList<>();
            for (int firstUserId : getUserById(id).getFriends()) {
                for (int secondUserId : getUserById(secondId).getFriends()) {
                    if (Objects.equals(firstUserId, secondUserId)) {
                        commonFriends.add(getUserById(firstUserId));
                    }
                }
            }
            return commonFriends;
        }
    }
}