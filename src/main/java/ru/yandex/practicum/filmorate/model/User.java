package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    int id;
    @Email(message = "не корректный e-mail") String email;
    @NotEmpty(message = "логин не может быть пустым") @NotBlank(message = "логин не может содержать пробелы") String login;
    String name;
    @Past(message = "день рождения не может быть в будущем") LocalDate birthday;

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
}

