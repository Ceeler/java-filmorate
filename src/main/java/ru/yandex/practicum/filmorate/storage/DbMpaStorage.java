package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class DbMpaStorage implements Storage<Mpa, Integer> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Mpa save(Mpa entity) {
        throw new RuntimeException("Сервис не реализован");
    }

    @Override
    public Optional<Mpa> get(Integer id) {
        String sql = "SELECT * FROM mpa WHERE id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<Mpa> mpa = jdbcTemplate.query(sql, parameterSource, (rs, rowNum) -> mapRow(rs, rowNum));
        if (mpa.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(mpa.get(0));
        }
    }

    @Override
    public Mpa update(Mpa entity) {
        throw new RuntimeException("Сервис не реализован");
    }

    @Override
    public void delete(Integer id) {
        throw new RuntimeException("Сервис не реализован");
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa ORDER BY id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs, rowNum));
    }

    private Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");

        if (id == 0) {
            return null;
        }

        return new Mpa(id, rs.getString("name"));
    }
}
