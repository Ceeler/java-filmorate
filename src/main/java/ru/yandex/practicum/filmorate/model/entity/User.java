package ru.yandex.practicum.filmorate.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

//    public User(long id, String email, String login, String name, LocalDate birthday, Set<User> friends) {
//        this.id = id;
//        this.email = email;
//        this.login = login;
//        this.name = name;
//        this.birthday = birthday;
//        this.friends = new HashSet<>();
//    }
}
