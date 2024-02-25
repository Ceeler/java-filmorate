package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface FilmStorage extends Storage<Film, Long> {

    void addLike(Film film, User user);

    void removeLike(Film film, User user);

    List<Film> getTopByLike(int count);

}
