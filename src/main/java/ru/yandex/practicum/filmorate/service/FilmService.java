package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {

    private static final LocalDate MIN_FILM_DATE = LocalDate.of(1895, 12, 28);

    private static final int DEFAULT_FILM_COUNT = 10;

    private final FilmStorage filmStorage;

    private final UserService userService;

    public FilmService(@Qualifier("DbFilmStorage") FilmStorage filmStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.get(id).orElseThrow(
                () -> new NotFoundException("Фильм с ID=" + id + "не найден"));
        return film;
    }

    public List<Film> getFilms() {
        return filmStorage.getAll();
    }

    public Film addFilm(Film film) {
        log.info("addFilm film={}", film);
        if (MIN_FILM_DATE.isAfter(film.getReleaseDate())) {
            throw new ConstraintViolationException("Фильм не может быть создан раньше 28 декабря 1895 года", null);
        }
        return filmStorage.save(film);
    }

    public Film updateFilm(Film film) {
        log.info("updateFilm film={}", film);
        filmStorage.get(film.getId()).orElseThrow(
                () -> new NotFoundException("Фильм с ID=" + film.getId() + "не найден"));

        if (MIN_FILM_DATE.isAfter(film.getReleaseDate())) {
            throw new ConstraintViolationException("Фильм не может быть создан раньше 28 декабря 1895 года", null);
        }

        Film response = filmStorage.update(film);
        return response;
    }

    public void addLike(Long id, Long userId) {
        Film film = filmStorage.get(id).orElseThrow(
                () -> new NotFoundException("Фильм с ID=" + id + "не найден"));
        User user = userService.getUserById(userId);
        filmStorage.addLike(film, user);
    }

    public void removeLike(Long id, Long userId) {
        Film film = filmStorage.get(id).orElseThrow(
                () -> new NotFoundException("Фильм с ID=" + id + "не найден"));
        User user = userService.getUserById(userId);
        filmStorage.removeLike(film, user);
    }

    public List<Film> getTopByLike(Integer count) {
        List<Film> films = filmStorage.getTopByLike(count == null ? DEFAULT_FILM_COUNT : count);
        return films;
    }
}