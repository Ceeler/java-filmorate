package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final Map<Long, User> users = new HashMap<>();
    private static long userSequence = 1;

    public User getUserById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException();
        }

        return user;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User addUser(User user) {
        log.info("addUser user={}", user);
        final long id = userSequence++;
        user.setId(id);
//        if (user.getName() == null) {
//            user.setName(user.getName());
//        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        users.put(id, user);
        return user;
    }

    public User updateUser(User user) {
        log.info("updateUser user={}", user);
        final long id = user.getId();
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException();
        }
//        if (user.getName() == null) {
//            user.setName(user.getName());
//        }
        users.put(id, user);
        return user;
    }
}
