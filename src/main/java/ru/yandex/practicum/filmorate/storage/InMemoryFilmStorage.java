package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap();
    private final ArrayList<Film> filmsArray = new ArrayList<>();
    private int idFilm;

    public ArrayList<Film> findAll() {
        filmsArray.clear();
        for (int id : films.keySet()) {
            filmsArray.add(films.get(id));
        }
        return filmsArray;
    }

    public void create(Film film) {
        idFilm = idFilm + 1;
        film.setId(idFilm);
        films.put(idFilm, film);
    }

    public void update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ObjectNotFoundException("Запись не добавлена");
        }
    }

    public Film getFilmById(int id) {
        if (films.get(id) != null) {
            return films.get(id);
        } else {
            throw new ObjectNotFoundException("Запись не добавлена");
        }
    }

    public void removeFilmById(int id) {
        if (films.get(id) != null) {
            films.remove(id);
        } else {
            throw new ObjectNotFoundException("Запись не удалена");
        }
    }
}