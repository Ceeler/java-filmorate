package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.entity.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

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
        log.info("Пришел Get запрос /users");
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
}
