package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;

import java.util.*;

@Component("InMemoryUserStorage")
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
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        }
        store.put(id, entity);
        return entity;
    }

    @Override
    public void delete(Long id) {
        User user = store.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с ID=" + id + " не найден");
        }
        store.remove(id);
        //return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void addFriend(User user, User friend) {

    }

    @Override
    public List<User> getFriendsByUser(User user) {
        return null;
    }

    @Override
    public void deleteFriend(User user, User friend) {

    }

    @Override
    public List<User> getUserCommonFriend(User user, User otherUser) {
        return null;
    }
}