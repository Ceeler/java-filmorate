package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> store;

    private long idSequence = 1;

    public InMemoryUserStorage() {
        this.store = new HashMap<>();
    }

    @Override
    public User save(User entity) {
        final long id = idSequence++;
        entity.setId(id);
        store.put(id, entity);
        return entity;
    }

    @Override
    public Optional<User> get(Long id) {
        User user = store.get(id);
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User entity) {
        final long id = entity.getId();
        User user = store.get(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID=" + id + " не найден");
        }
        store.put(id, entity);
        return entity;
    }

    @Override
    public User delete(Long id) {
        User user = store.get(id);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с ID=" + id + " не найден");
        }
        store.remove(id);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(store.values());
    }
}