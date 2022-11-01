package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreByID(int id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject("SELECT GENRE_ID, GENRE FROM GENRE WHERE GENRE_ID = ?",
                this::makeGenre, id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT GENRE_ID, GENRE FROM GENRE",
                this::makeGenre);
    }

    @Override
    public void createGenres(int genreId, int id) {
        String sqlQuery = "INSERT INTO FILM_GENRE (GENRE_ID, FILM_ID) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, genreId, id);
    }

    @Override
    public void deleteGenres(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }


    @Override
    public List<Genre> getFilmGenres(int id) {
        return jdbcTemplate.query("SELECT  g.GENRE_ID, g.GENRE " +
                        "FROM FILM_GENRE AS fg " +
                        "JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID  " +
                        "WHERE fg.FILM_ID = " + id +
                        " GROUP BY fg.GENRE_ID",
                this::makeGenre);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(
                rs.getInt("GENRE_ID"),
                rs.getString("GENRE")
        );
    }
}