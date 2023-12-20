package ru.yandex.practicum.filmorate.model.entity;

import lombok.*;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private long id;

    private String name;

    private String description;

    private LocalDate releaseDate;

    private Integer duration;
}
