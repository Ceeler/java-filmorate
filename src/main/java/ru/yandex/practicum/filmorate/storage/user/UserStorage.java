package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;

public interface UserStorage extends Storage<User,Long> {

    void addFriend(User user, User friend);

    List<User> getFriendsByUser(User user);

    void deleteFriend(User user, User friend);

    List<User> getUserCommonFriend(User user, User otherUser);
}