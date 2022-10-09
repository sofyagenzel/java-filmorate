package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

public interface FilmStorage {
    ArrayList<Film> findAll();

    void create(Film film);

    void update(Film film);

    Film getFilmById(int id);

    void removeFilmById(int id);

}