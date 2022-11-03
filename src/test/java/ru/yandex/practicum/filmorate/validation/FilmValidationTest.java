package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmValidationTest {

    @Test
    void validateTrue() {
        Film film = new Film(1, "фильм о волшебстве", "sofka", 35, LocalDate.of(2022, 01, 05),
                new MPA(1, "G"), List.of(new Genre(4, "Фантастика")));
        FilmValidation.validateFilm(film);
    }

    @Test
    void validateName() {
        Film film = new Film(1, "фильм о волшебстве", "", 35, LocalDate.of(2022, 01, 05),
                new MPA(1, "G"), List.of(new Genre(4, "Фантастика")));
        ValidationException thrown = assertThrows(ValidationException.class, () -> FilmValidation.validateFilm(film));
        assertTrue(thrown.getMessage().contains("Название не может быть пустым"));
    }

    @Test
    void validateLenDescr() {
        Film film = new Film(1, "ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
                "ttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt" +
                "ttttttttttttttttttttttttttttttttttttt",
                "sofka", 35, LocalDate.of(2022, 01, 05),
                new MPA(1, "G"), List.of(new Genre(4, "Фантастика")));
        ValidationException thrown = assertThrows(ValidationException.class, () -> FilmValidation.validateFilm(film));
        assertTrue(thrown.getMessage().contains("Максимальная длина описания - 200 символов"));
    }

    @Test
    void validateDuration() {
        Film film = new Film(1, "tt", "sofka", -35, LocalDate.of(2022, 01, 05),
                new MPA(1, "G"), List.of(new Genre(4, "Фантастика")));
        ValidationException thrown = assertThrows(ValidationException.class, () -> FilmValidation.validateFilm(film));
        assertTrue(thrown.getMessage().contains("Продолжительность фильма должна быть положительной"));
    }

    @Test
    void validateDate() {
        Film film = new Film(1, "tt", "sofka", 35, LocalDate.of(1701, 01, 05),
                new MPA(1, "G"), List.of(new Genre(4, "Фантастика")));
        ValidationException thrown = assertThrows(ValidationException.class, () -> FilmValidation.validateFilm(film));
        assertTrue(thrown.getMessage().contains("Дата релиза не раньше 28 декабря 1895 года"));
    }


}