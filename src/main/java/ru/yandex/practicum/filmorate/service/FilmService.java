package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final Comparator<Film> filmComp = new FilmComparatorLike();
    private final Comparator<Film> filmCompDate = new FilmComparatorDate();
    private final Set<Film> sortFilmList = new TreeSet<>(filmComp.thenComparing(filmCompDate));

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film addLikeFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id);
        film.addLike(userId);
        filmStorage.update(film);
        return film;
    }

    public Film deleteLikeFilm(int id, int userId) {
        if (filmStorage.getFilmById(id) == null) {
            throw new ObjectNotFoundException("Запись не найдена" + id);
        } else if (filmStorage.getFilmById(userId) == null) {
            throw new ObjectNotFoundException("Запись не найдена" + userId);
        } else {
            filmStorage.getFilmById(id).deleteLike(userId);
            return filmStorage.getFilmById(id);
        }
    }

    public List<Film> getPopularFilmList(int count) {
        sortFilmList.addAll(filmStorage.findAll());
        List<Film> listFilm = new ArrayList<>();
        for (Film film : sortFilmList) {
            if (listFilm.size() < count) {
                listFilm.add(film);
            }
        }
        return listFilm;
    }

    class FilmComparatorLike implements Comparator<Film> {
        @Override
        public int compare(Film a, Film b) {
            return b.getListLikes().size() - a.getListLikes().size();
        }
    }

    class FilmComparatorDate implements Comparator<Film> {
        @Override
        public int compare(Film a, Film b) {
            return b.getReleaseDate().compareTo(a.getReleaseDate());
        }
    }
}