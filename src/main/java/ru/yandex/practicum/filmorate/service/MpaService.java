package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.DbMpaStorage;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class MpaService {

    private final DbMpaStorage mpaStorage;

    public Mpa getMpaById(int id) {
        Mpa mpa = mpaStorage.get(id).orElseThrow(
                () -> new NotFoundException("Рейтинг MPA с ID=" + id + "не найден"));
        return mpa;
    }

    public List<Mpa> getMpa() {
        return mpaStorage.getAll();
    }

}
