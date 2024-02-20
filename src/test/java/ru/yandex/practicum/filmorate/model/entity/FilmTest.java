package ru.yandex.practicum.filmorate.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private Film film;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();


    private Validator validator = factory.getValidator();

    @BeforeEach
    public void beforeEach() {
        this.film = new Film(0L,"Титаник","Фильм о корабле", LocalDate.of(2023, 11, 2),124, new Mpa(124, "G"), new HashSet<>());
    }

    @Test
    public void shouldHaveZeroViolations() {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(0, violations.size());
    }

    @Test
    public void shouldHaveOneViolationOnEmptyName() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,  violations.size());
    }

    @Test
    public void shouldHaveOneViolationOnLongDescription() {
        String description = "";
        for (int i = 0; i < 201; i++) {
            description += "a";
        }
        film.setDescription(description);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,  violations.size());
    }

    @Test
    public void shouldHaveOneViolationOndNegativeDuration() {
        film.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1,  violations.size());
    }

    @Test
    public void shouldHaveThreeViolationsOnEmptyNameAndLongDescriptionAndNegativeDuration() {
        film.setName("");
        String description = "";
        for (int i = 0; i < 201; i++) {
            description += "a";
        }
        film.setDescription(description);
        film.setDuration(-100);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(3,  violations.size());
    }
}