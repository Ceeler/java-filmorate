package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.DbGenreStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class GenreService {

    private final DbGenreStorage genreStorage;

    public Genre getGenreById(int id) {
        Genre genre = genreStorage.get(id).orElseThrow(
                () -> new NotFoundException("Рейтинг MPA с ID=" + id + "не найден"));
        return genre;
    }

    public List<Genre> getGenres() {
        return genreStorage.getAll();
    }
}
