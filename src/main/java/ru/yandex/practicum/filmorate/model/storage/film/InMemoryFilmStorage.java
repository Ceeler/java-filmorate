package ru.yandex.practicum.filmorate.model.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    Map<Long, Film> store;

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
            throw new IllegalArgumentException();
        }
        store.put(id, entity);
        return entity;
    }

    @Override
    public Film delete(Long id) {
        Film film = store.get(id);
        if (film == null) {
            throw new IllegalArgumentException();
        }
        store.remove(id);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(store.values());
    }
}
