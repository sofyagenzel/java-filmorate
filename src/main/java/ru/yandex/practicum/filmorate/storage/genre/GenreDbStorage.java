package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) throws ObjectNotFoundException {
        String sqlQuery = "SELECT GENRE_ID, GENRE FROM GENRE WHERE GENRE_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException("Id жанра не найдено"));
    }

    @Override
    public List<Genre> getAllGenre() {
        String sqlQuery = "SELECT GENRE_ID, GENRE FROM GENRE";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public void createGenres(int filmId, List<Genre> genres) {
        List<Genre> genresUniq = genres.stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO FILM_GENRE (GENRE_ID, FILM_ID) VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement statement, int i) throws SQLException {
                        statement.setLong(1, genresUniq.get(i).getId());
                        statement.setLong(2, filmId);
                    }

                    public int getBatchSize() {
                        return genresUniq.size();
                    }
                }
        );
    }

    @Override
    public void deleteGenres(int filmId) {
        String sqlQuery = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?;";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Genre> getFilmGenres(int id) throws ObjectNotFoundException {
        String sqlQuery = "SELECT  g.GENRE_ID, g.GENRE " +
                "FROM FILM_GENRE AS fg " +
                "JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID  " +
                "WHERE fg.FILM_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs), id);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("GENRE_ID");
        String name = rs.getString("GENRE");
        return new Genre(id, name);
    }
}