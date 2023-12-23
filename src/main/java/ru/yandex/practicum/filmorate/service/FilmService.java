package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Film;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {

    private static final LocalDate MIN_FILM_DATE = LocalDate.of(1895, 12, 28);

//    private final Map<Long, Film> films = new HashMap<>(){{
//        put(1L, new Film(1, "Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2), 124));
//        put(2L, new Film(2, "Челюсти","Фильм об акуле", LocalDate.of(2023, 11, 2), 93));
//    }};

    private final Map<Long, Film> films = new HashMap<>();

    private static long filmSequence = 1;


    public Film getFilmById(long id) {
        Film film = films.get(id);
        if (film == null) {
            throw new IllegalArgumentException();
        }
        return film;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addFilm(Film film) {
        log.info("addFilm film={}", film);
        final long id = filmSequence++;
        if (MIN_FILM_DATE.isAfter(film.getReleaseDate())) {
            throw new ConstraintViolationException("Фильм не может быть создан раньше 28 декабря 1895 года", null);
        }
        film.setId(id);
        films.put(id, film);
        return film;
    }

    public Film updateFilm(Film film) {
        log.info("updateFilm film={}", film);
        final long id = film.getId();
        if (MIN_FILM_DATE.isAfter(film.getReleaseDate())) {
            throw new ConstraintViolationException("Фильм не может быть создан раньше 28 декабря 1895 года", null);
        }
        if (!films.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        films.put(id, film);
        return film;
    }

}