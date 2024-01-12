package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.model.response.Message;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        log.info("Пришел Get запрос /users/{}", id);
        User response = userService.getUserById(id);
        log.info("Отправлен ответ Get /users с телом: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
        log.info("Пришел Post запрос /users с телом: {}", user);
        User response = userService.addUser(user);
        log.info("Отправлен ответ Post /users с телом: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Пришел Put запрос /users с телом: {}", user);
        User response = userService.updateUser(user);
        log.info("Отправлен ответ Put /users с телом: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public Message addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пришел Put запрос /users/{}/friends/{}", id, friendId);
        userService.addFriend(id, friendId);
        Message response = new Message("Успешно!");
        log.info("Отправлен ответ Put /users/{}/friends/{} c телом {}", id, friendId, response);
        return response;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public Message deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пришел Delete запрос /users/{}/friends/{}", id, friendId);
        userService.removeFriend(id, friendId);
        Message response = new Message("Успешно!");
        log.info("Отправлен ответ Delete /users/{}/friends/{} c телом {}", id, friendId, response);
        return response;
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getFriends(@PathVariable long id) {
        log.info("Пришел Get запрос /users/{}/friends", id);
        Set<User> response = userService.getUserFriends(id);
        log.info("Отправлен ответ Get /users/{}/friends c телом {}", id, response);
        return response;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Set<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Пришел Get запрос /users/{}/friends/common/{}", id, otherId);
        Set<User> response = userService.getUserCommonFriend(id, otherId);
        log.info("Отправлен ответ Put /users/{}/friends/common/{} c телом {}", id, otherId, response);
        return response;
    }
}
