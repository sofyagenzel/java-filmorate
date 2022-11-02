package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreById(int id) throws ObjectNotFoundException;

    List<Genre> getAllGenre();

    void createGenres(int filmId, List<Genre> genres);

    List<Genre> getFilmGenres(int id) throws ObjectNotFoundException;

    void deleteGenres(int filmId);
}
