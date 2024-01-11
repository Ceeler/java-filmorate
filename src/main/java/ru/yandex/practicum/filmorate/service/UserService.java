package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.storage.user.UserStorage;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User getUserById(long id) {
        User user = userStorage.get(id).orElseThrow(() -> new IllegalArgumentException());
        return user;
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User addUser(User user) {
        log.info("addUser user={}", user);
        if (user.getName() == null) {
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
        User user = userStorage.get(id).orElseThrow(() -> new IllegalArgumentException());
        User friend = userStorage.get(friendId).orElseThrow(() -> new IllegalArgumentException());
        user.addFriend(friend);
        friend.addFriend(user);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.get(id).orElseThrow(() -> new IllegalArgumentException());
        User friend = userStorage.get(friendId).orElseThrow(() -> new IllegalArgumentException());
        user.removeFriend(friend);
        friend.removeFriend(user);
        userStorage.update(user);
        userStorage.update(friend);
    }
}