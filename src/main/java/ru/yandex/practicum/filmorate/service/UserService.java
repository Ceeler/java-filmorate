package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("DbUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(long id) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        log.info("addUser user={}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.save(user);
        return user;
    }

    public User updateUser(User user) {
        log.info("updateUser user={}", user);
        userStorage.get(user.getId()).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + user.getId() + "не найден"));
        userStorage.update(user);
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        User friend = userStorage.get(friendId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + friendId + "не найден"));
        userStorage.addFriend(user, friend);
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        User friend = userStorage.get(friendId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + friendId + "не найден"));
        userStorage.deleteFriend(user, friend);
    }

    public List<User> getUserFriends(Long id) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        return userStorage.getFriendsByUser(user);
    }

    public List<User> getUserCommonFriend(Long id, Long otherId) {
        final User user = userStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        final User otherUser = userStorage.get(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + otherId + "не найден"));

        return userStorage.getUserCommonFriend(user, otherUser);
    }
}