package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidationTest {

    @Test
    void validateTrue() {
        User user = new User(1, "sof@gmail.ru", "sofka", "sofka", LocalDate.of(2022, 01, 05));
        UserValidation.validateUser(user);
    }

    @Test
    void validateEmailDog() {
        User user = new User(1, "sofgmail.ru", "sofka", "sofka", LocalDate.of(2022, 01, 05));
        ValidationException thrown = assertThrows(ValidationException.class, () -> UserValidation.validateUser(user));
        assertTrue(thrown.getMessage().contains("Адрес электронной почты должен содержать символ @"));
    }

    @Test
    void validateLoginEmpty() {
        User user = new User(1, "sof@gmail.ru", "sofka", "", LocalDate.of(2022, 01, 05));
        ValidationException thrown = assertThrows(ValidationException.class, () -> UserValidation.validateUser(user));
        assertTrue(thrown.getMessage().contains("Логин не может быть пустым или содержать пробелы"));
    }

    @Test
    void validateLoginBlank() {
        User user = new User(1, "sof@gmail.ru", "sofka", " ", LocalDate.of(2022, 01, 05));
        ValidationException thrown = assertThrows(ValidationException.class, () -> UserValidation.validateUser(user));
        assertTrue(thrown.getMessage().contains("Логин не может быть пустым или содержать пробелы"));
    }

    @Test
    void validateBirthday() {
        User user = new User(1, "sof@gmail.ru", "sofka", "soff", LocalDate.of(2023, 01, 05));
        ValidationException thrown = assertThrows(ValidationException.class, () -> UserValidation.validateUser(user));
        assertTrue(thrown.getMessage().contains("День рождения не может быть в будущем"));
    }

    @Test
    void validateName() {
        User user = new User(1, "sof@gmail.ru", "", "sofka", LocalDate.of(2022, 01, 05));
        assertEquals(user.getName(), "sofka");
    }
}