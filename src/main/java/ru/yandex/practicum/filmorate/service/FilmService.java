package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.time.LocalDate;
import java.util.*;

@Service
public class FilmService {

    private final Map<Long, Film> films = new HashMap<>(){{
        put(1L, new Film(1, "Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2), 124));
        put(2L, new Film(2, "Челюсти","Фильм о акуле", LocalDate.of(2023, 11, 2), 93));
    }};

    private static long filmSequence = 2;


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
        final long id = filmSequence++;
        film.setId(id);
        films.put(id, film);
        return film;
    }

    public void updateFilm(Film film, long id) {
        if (!films.containsKey(id)) {
            throw new IllegalArgumentException();
        }
        film.setId(id);
        films.put(id, film);
    }

}