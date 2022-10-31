package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> findAll() {
        String sqlQuery = "select USER_ID, EMAIL, USERNAME, LOGIN, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("USERNAME"),
                        rs.getString("LOGIN"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ));
    }


    @Override
    public void create(User user) {
        String sqlQuery = "insert into USERS ( EMAIL, USERNAME, LOGIN,BIRTHDAY) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public void update(User user) {
        String sqlQuery = "update USERS set  EMAIL = ?, USERNAME = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getName()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
    }

    @Override
    public User getUserById(int id) {
        String sqlQuery = "select USER_ID, EMAIL, USERNAME, LOGIN,  BIRTHDAY from USERS where USER_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("USERNAME"),
                        rs.getString("LOGIN"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ), id);
    }

    @Override
    public void removeUserById(int id) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addNewFriend(int id, int friendId, String status) {
        String sqlQuery = "insert into FRIEND (USER_ID, FRIEND_ID, STATUS) values (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setInt(1, id);
            stmt.setInt(2, friendId);
            stmt.setString(3, status);
            return stmt;
        }, keyHolder);
        if (status.equals("подтвержден")) {
            String sqlQuery1 = "update FRIEND set  USER_ID = ?, FRIEND_ID = ?, STATUS = ?";
            jdbcTemplate.update(sqlQuery1
                    , friendId
                    , id
                    , status);
        }
    }

    @Override
    public void removeFriend(int id, int friendId) {
        String sqlQuery = "delete from FRIEND where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getListFriend(int id) {
        String sqlQuery = "select us.USER_ID, us.EMAIL, us.USERNAME, us.LOGIN,  us.BIRTHDAY "
                + "from USERS as u "
                + "JOIN FRIEND as f on u.USER_ID = f.USER_ID "
                + "JOIN USERS as us on f.FRIEND_ID = us.USER_ID "
                + "WHERE u.USER_ID = " + id;
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("USERNAME"),
                        rs.getString("LOGIN"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ));
    }

    @Override
    public List<User> getCommonFriends(int id, int secondId) {
        String sqlQuery = "select f.FRIEND_ID, u.USER_ID, u.EMAIL, u.USERNAME, u.LOGIN, u.BIRTHDAY "
                + "from FRIEND as f, FRIEND as fr "
                + "JOIN USERS as u on u.USER_ID = f.FRIEND_ID "
                + "where f.FRIEND_ID = fr.FRIEND_ID and f.USER_ID = " + id + " and fr.USER_ID = " + secondId;
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("USERNAME"),
                        rs.getString("LOGIN"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ));
    }

}
