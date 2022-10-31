package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmTest {
    private final FilmService filmService;
    private final UserService userService;


    @Order(1)
    @Test
    void getById_remove_true() {
        Film filmNew = Film.builder()
                .name("Rush")
                .description("about Rush")
                .duration(200)
                .releaseDate(LocalDate.parse("2013-07-03"))
                .mpa(MPA.builder().id(1).build())
                .build();

        int filmId = filmService.create(filmNew).getId();

        Optional<Film> filmOptional = Optional.ofNullable(filmService.getFilmById(filmId));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", filmId)
                );
        filmService.removeFilmById(filmId);
        List<Film> film = filmService.findAll();
        assertEquals(film.size(), 0);
    }

    @Order(2)
    @Test
    void getById_false() {
        assertThrows(EmptyResultDataAccessException.class, () -> filmService.getFilmById(6));
    }

    @Order(3)
    @Test
    public void getAll_update() {
        Film filmNew = Film.builder()
                .name("Rush")
                .description("about Rush")
                .duration(200)
                .releaseDate(LocalDate.parse("2013-07-03"))
                .mpa(MPA.builder().id(1).build())
                .build();

        int filmId = filmService.create(filmNew).getId();
        List<Film> films = filmService.findAll();
        assertEquals(films.size(), 1);

        Film filmNew1 = Film.builder()
                .id(filmId)
                .name("Rush")
                .description("about Rush")
                .duration(220)
                .releaseDate(LocalDate.parse("2013-07-03"))
                .mpa(MPA.builder().id(1).build())
                .build();

        Optional<Film> filmOptional = Optional.ofNullable(filmService.update(filmNew1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("duration", 220)
                );
    }

    @Order(4)
    @Test
    public void getPopularFilmList() {
        Film filmNew = Film.builder()
                .name("Rush")
                .description("about Rush")
                .duration(220)
                .releaseDate(LocalDate.parse("2013-07-03"))
                .mpa(MPA.builder().id(1).build())
                .build();

        Film filmNew1 = Film.builder()
                .name("Rush2")
                .description("about Rush")
                .duration(220)
                .releaseDate(LocalDate.parse("2013-07-03"))
                .mpa(MPA.builder().id(3).build())
                .build();
        filmService.create(filmNew);
        filmService.create(filmNew1);

        List<Film> film = filmService.getPopularFilmList(1);
        assertEquals(film.size(), 1);
    }

    @Order(5)
    @Test
    public void addLikeDeleteFilm() {
        Film filmNew = Film.builder()
                .name("Rush")
                .description("about Rush")
                .duration(220)
                .releaseDate(LocalDate.parse("2013-07-03"))
                .mpa(MPA.builder().id(1).build())
                .build();
        int filmId = filmService.create(filmNew).getId();

        User user = User.builder()
                .name("Sof")
                .email("785@gmail.com")
                .login("Sof7")
                .birthday(LocalDate.parse("1996-08-03"))
                .build();
        int userId = userService.create(user).getId();

        filmService.addLikeFilm(filmId, userId);
        filmService.deleteLikeFilm(filmId, userId);
    }
}
