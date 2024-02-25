package ru.yandex.practicum.filmorate.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private long id;

    @Email(message = "Не верный почтовый адрес")
    @NotBlank(message = "Почта не может быть пустой")
    private String email;

    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z]{4,16}$", message = "Некорректный никнейм")
    private String login;

    private String name;

    @Past(message = "День рождения не может быть в будущей дате")
    private LocalDate birthday;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<User> friends = new LinkedHashSet<>();

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public void removeFriend(User friend) {
        friends.remove(friend);
    }
}
