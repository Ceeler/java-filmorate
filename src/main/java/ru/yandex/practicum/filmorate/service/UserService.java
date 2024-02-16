package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

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
        userStorage.update(user);
        return user;
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        User friend = userStorage.get(friendId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + friendId + "не найден"));
        user.addFriend(friend);
        friend.addFriend(user);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        User friend = userStorage.get(friendId).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + friendId + "не найден"));
        user.removeFriend(friend);
        friend.removeFriend(user);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public Set<User> getUserFriends(Long id) {
        User user = userStorage.get(id).orElseThrow(
                () -> new NotFoundException("Пользователь с ID=" + id + "не найден"));
        return user.getFriends();
    }

    public Set<User> getUserCommonFriend(Long id, Long otherId) {
        final Set<User> userFriends = userStorage.get(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + id + "не найден"))
                .getFriends();
        final Set<User> otherFriends = userStorage.get(otherId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID=" + otherId + "не найден"))
                .getFriends();

        if (otherFriends.size() == 0) {
            return Collections.emptySet();
        }
        Set<User> commonFriends = userFriends.stream()
                .filter(friend -> otherFriends.contains(friend))
                .collect(Collectors.toSet());
        return commonFriends;
    }
}