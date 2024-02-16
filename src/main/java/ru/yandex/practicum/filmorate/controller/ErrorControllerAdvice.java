package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.model.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.response.Message;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Message> onConstraintException(ConstraintViolationException e) {
        log.warn("Ошибка валидации", e);
        if (e.getConstraintViolations() != null) {
            List<Message> response = e.getConstraintViolations().stream()
                    .map(v -> new Message(v.getMessage()))
                    .collect(Collectors.toList());
            return response;
        }
        return Collections.singletonList(new Message(e.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Message onNotFoundException(NotFoundException e) {
        log.warn("404 {}", e.getMessage());
        return new Message(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Message handleThrowable(final Throwable e) {
        log.warn("500 {}", e.getMessage());
        return new Message(e.getMessage());
    }
}
