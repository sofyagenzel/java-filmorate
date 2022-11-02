package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class MPADbStorage implements MPAStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MPADbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMPAByID(int id) throws ObjectNotFoundException {
        String sqlQuery = "SELECT MPA_ID, MPA FROM MPA  WHERE MPA_ID = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMPA(rs), id)
                .stream()
                .findAny()
                .orElseThrow(() -> new ObjectNotFoundException("Id MPA не найден"));
    }

    @Override
    public List<MPA> getAllMPA() {
        String sqlQuery = "SELECT MPA_ID, MPA FROM MPA";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMPA(rs));
    }

    private MPA makeMPA(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("MPA_ID");
        String name = rs.getString("MPA");
        return new MPA(id, name);
    }
}
