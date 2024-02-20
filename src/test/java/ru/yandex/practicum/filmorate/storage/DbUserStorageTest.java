package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User user = new User(0L, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        Long id = userStorage.save(user).getId();

        User savedUser = userStorage.get(id).orElse(null);
        user.setId(id);

        assertAll("sameUser",
                () -> assertNotNull(savedUser),
                () -> assertEquals(user, savedUser)
        );
    }

    @Test
    public void testUpdateUser() {
        User user = new User(2L, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());
        User newUser = new User(2L, "newlosev@danil-m.ru", "newCeeler", "newДанил", LocalDate.of(1999, 12, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        Long id = userStorage.save(user).getId();
        newUser.setId(id);
        userStorage.update(newUser).getId();

        User savedUser = userStorage.get(id).orElse(null);

        assertAll("sameUser",
                () -> assertNotNull(savedUser),
                () -> assertEquals(newUser, savedUser)
        );
    }

    @Test
    public void testDeleteUser() {
        User user = new User(3L, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        Long id = userStorage.save(user).getId();
        userStorage.delete(id);

        User savedUser = userStorage.get(id).orElse(null);
        assertNull(savedUser);
    }

    @Test
    public void testAddFriend() {
        User user = new User(4L, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());
        User friend = new User(5L, "newlosev@danil-m.ru", "newCeeler", "newДанил", LocalDate.of(1999, 12, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        Long id = userStorage.save(user).getId();
        user.setId(id);
        Long friendId = userStorage.save(friend).getId();
        friend.setId(friendId);

        userStorage.addFriend(user, friend);

        List<User> friends = userStorage.getFriendsByUser(user);

        assertAll("haveFriend",
                () -> assertEquals(1, friends.size()),
                () -> assertEquals(friend, friends.get(0))
        );
    }

    @Test
    public void testDeleteFriend() {
        User user = new User(4L, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());
        User friend = new User(5L, "newlosev@danil-m.ru", "newCeeler", "newДанил", LocalDate.of(1999, 12, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        Long id = userStorage.save(user).getId();
        user.setId(id);
        Long friendId = userStorage.save(friend).getId();
        friend.setId(friendId);

        userStorage.addFriend(user, friend);
        userStorage.deleteFriend(user, friend);

        List<User> friends = userStorage.getFriendsByUser(user);

        assertEquals(0, friends.size());
    }

    @Test
    public void testCommonFriend() {
        User user = new User(4L, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());
        User user2 = new User(5L, "newlosev@danil-m.ru", "newCeeler", "newДанил", LocalDate.of(1999, 12, 13), new HashSet<>());
        User friend = new User(5L, "newlosev@danil-m.ru", "newCeeler", "newДанил", LocalDate.of(1999, 12, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        Long id = userStorage.save(user).getId();
        user.setId(id);
        Long id2 = userStorage.save(user2).getId();
        user2.setId(id2);
        Long friendId = userStorage.save(friend).getId();
        friend.setId(friendId);

        userStorage.addFriend(user, friend);
        userStorage.addFriend(user2, friend);

        List<User> friends = userStorage.getUserCommonFriend(user, user2);

        assertAll("haveFriend",
                () -> assertEquals(1, friends.size()),
                () -> assertEquals(friend, friends.get(0))
        );
    }
}
