package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.model.response.Message;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ConstraintControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<Message>> onConstraintException(ConstraintViolationException e) {
        log.warn("Ошибка валидации", e);
        if (e.getConstraintViolations() != null) {
            List<Message> response = e.getConstraintViolations().stream()
                    .map(v -> new Message(v.getMessage()))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Collections.singletonList(new Message(e.getMessage())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Message> onIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Ошибка в аргументах запроса", e);
        return new ResponseEntity<>(new Message(e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
