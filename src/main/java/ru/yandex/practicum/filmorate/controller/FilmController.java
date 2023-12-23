package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.response.Message;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/film")
@AllArgsConstructor
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok(filmService.getFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathParam("id") long id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.info("POST addFilm body={}", film);
        return ResponseEntity.ok(filmService.addFilm(film));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@RequestBody Film film, @PathParam("id") long id) {
        log.info("PUT updateFilm body={}", film);
        Film response = filmService.updateFilm(film, id);
        return ResponseEntity.ok(response);
    }
}