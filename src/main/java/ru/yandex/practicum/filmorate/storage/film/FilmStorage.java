package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> findAll();

    void create(Film film);

    void update(Film film);

    Film getFilmById(int id);

    void removeFilmById(int id);

    List<Film> getPopularFilmList(int count);

    void addLikeFilm(int id, int userId);

    void deleteLikeFilm(int id, int userId);
}