package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
public class User {
    int id;
    @Email(message = "не корректный e-mail") String email;
    @NotEmpty(message = "логин не может быть пустым") @NotBlank(message = "логин не может содержать пробелы") String login;
    String name;
    @Past(message = "день рождения не может быть в будущем") LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();


    public User() {
    }

    public User(int id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name.isEmpty()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }

    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }
}