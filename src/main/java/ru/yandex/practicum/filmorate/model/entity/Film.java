package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;

    @NotBlank(message = "Имя фильма не может быть пустой")
    private String name;

    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    private String description;


    private LocalDate releaseDate;

    @Positive
    private Integer duration;
}