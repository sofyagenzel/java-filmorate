package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap();
    private final ArrayList<Film> filmsArray = new ArrayList<>();
    private int idFilm;

    @GetMapping
    public ArrayList<Film> findAll() {
        for (int id : films.keySet()) {
            filmsArray.add(films.get(id));
        }
        return filmsArray;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Валидация пройдена.");
        idFilm = idFilm + 1;
        film.setId(idFilm);
        films.put(idFilm, film);
        log.info("Добавлен фильм.");
        return film;
    }

    @PutMapping
    public Film upload(@Valid @RequestBody Film film) {
        log.info("Валидация пройдена.");
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлена информация о фильме.");
        } else {
            throw new ValidationException("Запись не добавлена");
        }
        return film;
    }
}
