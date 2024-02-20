package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DbGenreStorage implements Storage<Genre, Integer> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Genre save(Genre entity) {
        throw new RuntimeException("Сервис не реализован");
    }

    @Override
    public Optional<Genre> get(Integer id) {
        String sql = "SELECT * FROM genres WHERE id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<Genre> genre = jdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> mapRow(rs, rowNum));
        if(genre.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(genre.get(0));
        }
    }

    @Override
    public Genre update(Genre entity) {
        throw new RuntimeException("Сервис не реализован");
    }

    @Override
    public void delete(Integer id) {
        throw new RuntimeException("Сервис не реализован");
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs, rowNum));
    }

    private Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");

        if (id == 0) {
            return null;
        }
        return new Genre(id, rs.getString("name"));
    }
}
