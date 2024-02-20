package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFilmStorageTest {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
       Film film = new Film(0l,"Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2),124, new Mpa(1, "G"), new HashSet<>());

        DbFilmStorage filmStorage = new DbFilmStorage(jdbcTemplate);

        Long id = filmStorage.save(film).getId();
        film.setId(id);

        Film savedFilm = filmStorage.get(id).orElse(null);

        assertAll("sameFilm",
                () -> assertNotNull(savedFilm),
                () -> assertEquals(film, savedFilm)
        );
    }

    @Test
    public void testUpdateFilm() {
        Film film = new Film(0l,"Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2),124, new Mpa(1, "G"), new HashSet<>());
        Film newFilm = new Film(0l,"newТитаник","newФильм о корабле", LocalDate.of(2024, 11, 2),224, new Mpa(2, "PG"), new HashSet<>());

        DbFilmStorage filmStorage = new DbFilmStorage(jdbcTemplate);

        Long id = filmStorage.save(film).getId();
        film.setId(id);
        newFilm.setId(id);
        filmStorage.update(newFilm);

        Film savedFilm = filmStorage.get(id).orElse(null);

        assertAll("sameFilm",
                () -> assertNotNull(savedFilm),
                () -> assertEquals(newFilm, savedFilm)
        );
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film(0l,"Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2),124, new Mpa(1, "G"), new HashSet<>());

        DbFilmStorage filmStorage = new DbFilmStorage(jdbcTemplate);

        Long id = filmStorage.save(film).getId();

        film.setId(id);
        filmStorage.delete(id);

        Film savedFilm = filmStorage.get(id).orElse(null);

        assertNull(savedFilm);
    }

    @Test
    public void testAddLike() {
        Film film = new Film(0l,"Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2),124, new Mpa(1, "G"), new HashSet<>());
        Film film2 = new Film(0l,"newТитаник","newФильм о корабле", LocalDate.of(2024, 11, 2),224, new Mpa(2, "PG"), new HashSet<>());
        User user = new User(0, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        DbFilmStorage filmStorage = new DbFilmStorage(jdbcTemplate);

        Long userId = userStorage.save(user).getId();
        Long id = filmStorage.save(film).getId();
        Long id2 = filmStorage.save(film2).getId();
        film.setId(id);
        film2.setId(id2);
        user.setId(userId);

        filmStorage.addLike(film, user);

        List<Film> films = filmStorage.getTopByLike(1);

        assertAll("checkTop",
                () -> assertEquals(1, films.size()),
                () -> assertEquals(film, films.get(0))
        );
    }

    @Test
    public void testRemoveLike() {
        Film film = new Film(0l,"Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2),124, new Mpa(1, "G"), new HashSet<>());
        Film film2 = new Film(0l,"newТитаник","newФильм о корабле", LocalDate.of(2024, 11, 2),224, new Mpa(2, "PG"), new HashSet<>());
        User user = new User(0, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());
        User user2 = new User(0, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());

        DbUserStorage userStorage = new DbUserStorage(jdbcTemplate);
        DbFilmStorage filmStorage = new DbFilmStorage(jdbcTemplate);

        Long userId = userStorage.save(user).getId();
        Long user2Id = userStorage.save(user2).getId();
        Long id = filmStorage.save(film).getId();
        Long id2 = filmStorage.save(film2).getId();
        film.setId(id);
        film2.setId(id2);
        user.setId(userId);
        user2.setId(user2Id);

        filmStorage.addLike(film, user);
        filmStorage.addLike(film, user2);
        filmStorage.addLike(film2, user);
        filmStorage.addLike(film2, user2);
        filmStorage.removeLike(film, user2);

        List<Film> films = filmStorage.getTopByLike(1);

        assertAll("checkTop",
                () -> assertEquals(1, films.size()),
                () -> assertEquals(film2, films.get(0))
        );
    }
}
