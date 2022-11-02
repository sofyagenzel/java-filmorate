package ru.yandex.practicum.filmorate.model.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MPAStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;


    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MPAStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public void create(Film film) {
        String sqlQuery = "INSERT INTO FILM ( NAME, DESCRIPTION, RELEASE, DURATION,MPA_ID) " +
                "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        if (film.getGenres() != null) {
            genreStorage.createGenres(film.getId(), film.getGenres());
        }
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "UPDATE FILM " +
                "SET NAME = ?, DESCRIPTION = ?, RELEASE = ?, DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName()
                , film.getDescription()
                , Date.valueOf(film.getReleaseDate())
                , film.getDuration()
                , film.getMpa().getId()
                , film.getId());
        genreStorage.deleteGenres(film.getId());
        if (film.getGenres() != null) {
            genreStorage.createGenres(film.getId(), film.getGenres());
        }
    }

    @Override
    public Film getFilmById(int id) throws ObjectNotFoundException {
        String sqlQuery = "SELECT FILM_ID, NAME, RELEASE, DESCRIPTION, DURATION, MPA_ID " +
                "FROM FILM " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException("Id фильма не найден"));
    }

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT FILM_ID, NAME, RELEASE, DESCRIPTION, DURATION,MPA_ID " +
                "FROM FILM";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void removeFilmById(int id) throws EmptyResultDataAccessException {
        String sqlQuery = "DELETE FROM FILM WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Film> getPopularFilmList(int count) {
        String sqlQuery = "SELECT FILM.FILM_ID, FILM.MPA_ID, FILM.NAME, FILM.RELEASE,FILM.DESCRIPTION, " +
                "FILM.DURATION, COUNT(LIKES.USER_ID) " +
                "FROM FILM " +
                "LEFT JOIN LIKES ON FILM.FILM_ID = LIKES.FILM_ID " +
                "GROUP BY FILM.FILM_ID, LIKES.USER_ID " +
                "ORDER BY LIKES.USER_ID DESC  " +
                "LIMIT " + count;
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs));
    }

    @Override
    public void addLikeFilm(int id, int userId) throws ObjectNotFoundException {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"LIKE_ID"});
            stmt.setInt(1, id);
            stmt.setInt(2, userId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) throws ObjectNotFoundException {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, id, userId) == 0) {
            throw new ObjectNotFoundException("лайк не найден");
        }
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        return new Film(
                rs.getInt("FILM_ID"),
                rs.getString("DESCRIPTION"),
                rs.getString("NAME"),
                rs.getInt("DURATION"),
                rs.getDate("RELEASE").toLocalDate(),
                mpaStorage.getMPAByID(rs.getInt("MPA_ID")),
                genreStorage.getFilmGenres(rs.getInt("FILM_ID"))
        );
    }

}

