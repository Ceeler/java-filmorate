package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.model.response.Message;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

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

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Message putLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пришел Put запрос /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        Message response = new Message("Успешно!");
        log.info("Отправлен ответ Put /films/{}/like/{}", id, userId, response);
        return response;
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пришел Delete запрос /films/{}/like/{}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Пришел Get запрос /films/popular");
        List<Film> response = filmService.getTopByLike(count);
        log.info("Отправлен ответ Get /films/popular с телом: {}", response);
        return response;
    }
}
