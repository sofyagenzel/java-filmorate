package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre getGenreByID(int id);

    List<Genre> getAllGenre();

    void createGenres(int genreId, int id);

    List<Genre> getFilmGenres(int id);

    void deleteGenres(int filmId);
}
