package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMPAByID(int id) throws EmptyResultDataAccessException {
        return jdbcTemplate.queryForObject("SELECT MPA_ID, MPA FROM MPA  WHERE MPA_ID = ?",
                this::makeMPA, id);
    }

    @Override
    public List<MPA> getAllMPA() {
        return jdbcTemplate.query("SELECT MPA_ID, MPA FROM MPA",
                this::makeMPA);
    }

    private MPA makeMPA(ResultSet rs, int rowNum) throws SQLException {
        return new MPA(
                rs.getInt("MPA_ID"),
                rs.getString("MPA")
        );
    }
}
