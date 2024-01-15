package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T, K> {

    T save(T entity);

    Optional<T> get(K id);

    T update(T entity);

    T delete(K id);

    List<T> getAll();

}