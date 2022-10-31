package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest {
    private final UserService userService;

    @Order(1)
    @Test
    void getById_remove_true() {
        User user = User.builder()
                .name("Sof")
                .email("785@gmail.com")
                .login("Sof7")
                .birthday(LocalDate.parse("1996-08-03"))
                .build();
        int userId = userService.create(user).getId();

        Optional<User> filmOptional = Optional.ofNullable(userService.getUserById(userId));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", userId)
                );
        userService.removeUserById(userId);
        List<User> users = userService.findAll();
        assertEquals(0,users.size());
    }

    @Order(2)
    @Test
    void getById_false() {
        assertThrows(EmptyResultDataAccessException.class, () -> userService.getUserById(7));
    }

    @Order(3)
    @Test
    public void getAll_update() {
        User user = User.builder()
                .name("Sof")
                .email("785@gmail.com")
                .login("Sof7")
                .birthday(LocalDate.parse("1996-08-03"))
                .build();

        int userId = userService.create(user).getId();
        List<User> users = userService.findAll();
        assertEquals(1,users.size());

        User user1 = User.builder()
                .id(userId)
                .name("Sofa")
                .email("785@gmail.com")
                .login("Sof7")
                .birthday(LocalDate.parse("1996-08-03"))
                .build();

        Optional<User> filmOptional = Optional.ofNullable(userService.update(user1));
        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Sofa")
                );
    }

    @Order(4)
    @Test
    public void addRemoveGetFriend() {
        User user = User.builder()
                .name("Sof")
                .email("785@gmail.com")
                .login("Sof7")
                .birthday(LocalDate.parse("1996-08-03"))
                .build();

        int userId = userService.create(user).getId();

        User friend = User.builder()
                .name("Ann")
                .email("222@gmail.com")
                .login("Ann4")
                .birthday(LocalDate.parse("1996-09-03"))
                .build();

        int friendId = userService.create(friend).getId();

        userService.addNewFriend(userId, friendId);
        List<User> listFriend = userService.getListFriend(userId);
        assertEquals(listFriend.size(), 1);
        userService.removeFriend(userId, friendId);
        listFriend = userService.getListFriend(userId);
        assertEquals(listFriend.size(), 0);

    }

    @Order(5)
    @Test
    public void getCommonFriends() {
        User user = User.builder()
                .name("Sof")
                .email("785@gmail.com")
                .login("Sof7")
                .birthday(LocalDate.parse("1996-08-03"))
                .build();

        int userId = userService.create(user).getId();

        User friend = User.builder()
                .name("Ann")
                .email("222@gmail.com")
                .login("Ann4")
                .birthday(LocalDate.parse("1996-09-03"))
                .build();

        int friendId = userService.create(friend).getId();

        User commonFriend = User.builder()
                .name("Julia")
                .email("888@gmail.com")
                .login("Juli")
                .birthday(LocalDate.parse("1996-10-03"))
                .build();

        int commonFriendId = userService.create(commonFriend).getId();

        userService.addNewFriend(userId, commonFriendId);
        userService.addNewFriend(friendId, commonFriendId);

        List<User> listFriend = userService.getCommonFriends(userId, friendId);
        assertEquals(listFriend.get(0).getName(), "Julia");

    }
}
