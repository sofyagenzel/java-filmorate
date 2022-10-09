package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer, User> users = new HashMap();
    private final ArrayList<User> usersArray = new ArrayList<>();
    private int idUser = 0;

    public ArrayList<User> findAll() {
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
}