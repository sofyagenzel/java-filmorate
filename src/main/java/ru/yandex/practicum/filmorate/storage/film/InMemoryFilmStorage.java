package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> films = new HashMap();
    private final ArrayList<Film> filmsArray = new ArrayList<>();
    private int idFilm;
    private final Comparator<Film> filmComp = new FilmComparatorLike();
    private final Comparator<Film> filmCompDate = new FilmComparatorDate();
    private final Set<Film> sortFilmList = new TreeSet<>(filmComp.thenComparing(filmCompDate));

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

    public void addLikeFilm(int id, int userId) {
        Film film = getFilmById(id);
        film.addLike(userId);
        update(film);

    }

    public void deleteLikeFilm(int id, int userId) {
        if (getFilmById(id) == null) {
            throw new ObjectNotFoundException("Запись не найдена" + id);
        } else if (getFilmById(userId) == null) {
            throw new ObjectNotFoundException("Запись не найдена" + userId);
        } else {
            getFilmById(id).deleteLike(userId);
        }
    }

    public List<Film> getPopularFilmList(int count) {
        sortFilmList.addAll(findAll());
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