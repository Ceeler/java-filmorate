package ru.yandex.practicum.filmorate.model.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<Klass, I> {

    Klass save(Klass entity);

    Optional<Klass> get(I id);

    Klass update(Klass entity);

    Klass delete(I id);

    List<Klass> getAll();

}