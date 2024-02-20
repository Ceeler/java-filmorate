package ru.yandex.practicum.filmorate.storage.user;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component("DbUserStorage")
@AllArgsConstructor
public class DbUserStorage implements UserStorage{

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public User save(User entity) {
        String sql = "INSERT INTO users (email, login, name, birthday) VALUES(:email, :login, :name, :birthday)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("email", entity.getEmail())
                .addValue("login", entity.getLogin())
                .addValue("name", entity.getName())
                .addValue("birthday", entity.getBirthday());

        jdbcTemplate.update(sql, parameterSource, keyHolder);

        final Long id = keyHolder.getKeyAs(Long.class);
        entity.setId(id);
        return entity;
    }

    @Override
    public Optional<User> get(Long id) {
        String sql = "SELECT * FROM users WHERE id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<User> user = jdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> mapRow(rs, rowNum));

        if (user.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(user.get(0));
        }
    }

    @Override
    public User update(User entity) {
        String sql = "UPDATE users SET email=:email, login=:login, name=:name, birthday=:birthday WHERE id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", entity.getId())
                .addValue("email", entity.getEmail())
                .addValue("login", entity.getLogin())
                .addValue("name", entity.getName())
                .addValue("birthday", entity.getBirthday());
        jdbcTemplate.update(sql, parameterSource);
        return entity;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs, rowNum));
    }

    @Override
    public void addFriend(User user, User friend) {
        String sql = "INSERT INTO user_user(user_id, friend_id) VALUES (:user_id, :friend_id)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("friend_id", friend.getId());
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public List<User> getFriendsByUser(User user) {
        String sql = "SELECT * FROM user_user AS uu " +
                "INNER JOIN users AS u ON uu.friend_id=u.id " +
                "WHERE uu.user_id=:user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("user_id", user.getId());
        return jdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> mapRow(rs, rowNum));
    }

    @Override
    public void deleteFriend(User user, User friend) {
        String sql = "DELETE FROM user_user " +
                "WHERE (user_id=:user_id AND friend_id=:friend_id) OR (user_id=:friend_id AND friend_id=:user_id)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("friend_id", friend.getId());
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public List<User> getUserCommonFriend(User user, User otherUser) {
        String sql = "SELECT * FROM user_user AS uu " +
                     "INNER JOIN users AS u ON uu.friend_id=u.id " +
                     "WHERE uu.user_id=:user_id AND " +
                     "uu.friend_id IN (SELECT friend_id FROM user_user WHERE user_id=:other_id)";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("user_id", user.getId())
                .addValue("other_id", otherUser.getId());

        return jdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> mapRow(rs, rowNum));
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        if (id == 0) {
            return null;
        }

        return User.builder()
                .id(id)
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
