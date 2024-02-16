package ru.yandex.practicum.filmorate.model.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();


    private Validator validator = factory.getValidator();

    @BeforeEach
    public void beforeEach() {
        this.user =  new User(1, "losev@danil-m.ru", "Ceeler", "Данил", LocalDate.of(1999, 11, 13), new HashSet<>());
    }

    @Test
    public void shouldHaveZeroViolations() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size());
    }

    @Test
    public void shouldHaveOneViolationOnEmptyMail() {
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1,  violations.size());
    }

    @Test
    public void shouldHaveOneViolationOnWrongMail() {
        user.setEmail("ya.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1,  violations.size());
    }

    @Test
    public void shouldHaveTwoViolationOnEmptyLogin() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2,  violations.size());
    }

    @Test
    public void shouldHaveOneViolationOnLoginWithSpace() {
        user.setLogin("My login");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1,  violations.size());
    }

//    @Test
//    public void shouldUseLoginOnEmptyName() {
//        User user1 = new User("losev@danil-m.ru", "Ceeler", LocalDate.of(1999, 11, 13));
//        assertEquals("Ceeler",  user1.getName());
//    }

    @Test
    public void shouldHaveOneViolationOnFutureBirthday() {
        user.setBirthday(LocalDate.of(2024,11,13));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1,  violations.size());
    }

    @Test
    public void shouldHaveTreeViolationOnFutureBirthdayAndWrongLoginAndEmptyMail() {
        user.setEmail("");
        user.setLogin("My login");
        user.setBirthday(LocalDate.of(2024,11,13));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(3,  violations.size());
    }

}