package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> store;

    private long idSequence = 1;

    public InMemoryFilmStorage() {
        this.store = new HashMap<>();
    }

    @Override
    public Film save(Film entity) {
        final long id = idSequence++;
        entity.setId(id);
        store.put(id, entity);
        return entity;
    }

    @Override
    public Optional<Film> get(Long id) {
        Film film = store.get(id);
        return Optional.ofNullable(film);
    }

    @Override
    public Film update(Film entity) {
        final long id = entity.getId();
        Film film = store.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с ID=" + id + " не найден");
        }
        store.put(id, entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        Film film = store.get(id);
        if (film == null) {
            throw new NotFoundException("Фильм с ID=" + id + " не найден");
        }
        store.remove(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void addLike(Film film, User user) {

    }

    @Override
    public void removeLike(Film film, User user) {

    }

    @Override
    public List<Film> getTopByLike(int count) {
        return null;
    }
}
