package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@AllArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(filmService.getFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable("id") long id) {
        log.info("Пришел Get запрос /films");
        Film response = filmService.getFilmById(id);
        log.info("Отправлен ответ Get /films с телом: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        log.info("Пришел Post запрос /films с телом: {}", film);
        Film response = filmService.addFilm(film);
        log.info("Отправлен ответ Post /films с телом: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Пришел Put запрос /films с телом: {}", film);
        Film response = filmService.updateFilm(film);
        log.info("Отправлен ответ Put /films с телом: {}", response);
        return ResponseEntity.ok(response);
    }
}
