package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.storage.film.FilmStorage;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class FilmService {

    private static final LocalDate MIN_FILM_DATE = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;

    public Film getFilmById(long id) {
        Film film = filmStorage.get(id).orElseThrow(() -> new IllegalArgumentException());
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
        if (MIN_FILM_DATE.isAfter(film.getReleaseDate())) {
            throw new ConstraintViolationException("Фильм не может быть создан раньше 28 декабря 1895 года", null);
        }
        Film response = filmStorage.update(film);
        return response;
    }

}