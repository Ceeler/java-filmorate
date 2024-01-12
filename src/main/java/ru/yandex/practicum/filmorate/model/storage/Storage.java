package ru.yandex.practicum.filmorate.model.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<С, I> {

    С save(С entity);

    Optional<С> get(I id);

    С update(С entity);

    С delete(I id);

    List<С> getAll();

}