package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        filmStorage.create(film);
        return film;
    }

    public Film update(Film film) {
        filmStorage.update(film);
        return getFilmById(film.getId());
    }

    public Film getFilmById(int id) {
        if (filmStorage.getFilmById(id) != null) {
            return filmStorage.getFilmById(id);
        } else {
            throw new ObjectNotFoundException("Запись не найдена");
        }
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public void removeFilmById(int id) {
        if (filmStorage.getFilmById(id) != null) {
            filmStorage.removeFilmById(id);
        } else {
            throw new ObjectNotFoundException("Запись не удалена");
        }
    }

    public List<Film> getPopularFilmList(int count) {
        return filmStorage.getPopularFilmList(count);
    }

    public void addLikeFilm(int id, int userId) {
        filmStorage.addLikeFilm(id, userId);
    }

    public void deleteLikeFilm(int id, int userId) {

        filmStorage.deleteLikeFilm(id, userId);
    }
}