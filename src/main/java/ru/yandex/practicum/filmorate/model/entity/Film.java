package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private long id;

    @NotBlank(message = "Имя фильма не может быть пустой")
    private String name;

    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive
    private Integer duration;

    private Mpa mpa;

    private Set<Genre> genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));

}
