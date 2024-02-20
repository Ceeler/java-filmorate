package ru.yandex.practicum.filmorate.storage.film;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.entity.Film;
import ru.yandex.practicum.filmorate.model.entity.Genre;
import ru.yandex.practicum.filmorate.model.entity.Mpa;
import ru.yandex.practicum.filmorate.model.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component("DbFilmStorage")
@AllArgsConstructor
public class DbFilmStorage implements FilmStorage {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public Film save(Film entity) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                     "VALUES (:name, :description, :release_date, :duration, :mpa_id)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("release_date", entity.getReleaseDate())
                .addValue("duration", entity.getDuration())
                .addValue("mpa_id", entity.getMpa().getId());
        jdbcTemplate.update(sql, parameterSource, keyHolder);

        final Long id = keyHolder.getKeyAs(Long.class);
        entity.setId(id);

        addGenres(entity.getId(), entity.getGenres());

        return entity;
    }

    @Override
    public Optional<Film> get(Long id) {
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.mpa_id, " +
                     "f.duration, fg.genre_id, g.name AS genre_name, m.NAME AS mpa_name " +
                     "FROM films AS f " +
                     "LEFT JOIN film_genre AS fg ON f.id=fg.film_id " +
                     "LEFT JOIN genres AS g ON fg.genre_id=g.id " +
                     "LEFT JOIN mpa AS m ON f.mpa_id=m.id " +
                     "WHERE f.id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        List<Film> films = jdbcTemplate.query(sql, parameterSource, new FilmExtractor());

        return films.stream().findFirst();
    }

    @Override
    public Film update(Film entity) {
        String sql = "UPDATE films SET name=:name, description=:description, " +
                "release_date=:release_date, duration=:duration, mpa_id=:mpa_id " +
                "WHERE id=:film_id";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", entity.getId())
                .addValue("name", entity.getName())
                .addValue("description", entity.getDescription())
                .addValue("release_date", entity.getReleaseDate())
                .addValue("duration", entity.getDuration())
                .addValue("mpa_id", entity.getMpa().getId());

        jdbcTemplate.update(sql, parameterSource);

        String subSql = "DELETE FROM film_genre WHERE film_id=:film_id";
        SqlParameterSource subParametrSource = new MapSqlParameterSource("film_id", entity.getId());
        jdbcTemplate.update(subSql, subParametrSource);

        addGenres(entity.getId(), entity.getGenres());

        // Не проходил тест из-за порядка жанров
        TreeSet<Genre> genresSet = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        genresSet.addAll(entity.getGenres());
        entity.setGenres(genresSet);

        return entity;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM films WHERE id=:id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.mpa_id, " +
                     "f.duration, fg.genre_id, g.name AS genre_name, m.name AS mpa_name " +
                     "FROM films AS f " +
                     "LEFT JOIN film_genre AS fg ON f.id=fg.film_id " +
                     "LEFT JOIN genres AS g ON fg.genre_id=g.id " +
                     "LEFT JOIN mpa AS m ON f.mpa_id=m.id";
        List<Film> films = jdbcTemplate.query(sql, new FilmExtractor());

        return films;
    }

    @Override
    public void addLike(Film film, User user) {
        String sql = "INSERT INTO film_likes(film_id, user_id) VALUES (:film_id, :user_id)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", film.getId())
                .addValue("user_id", user.getId());
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public void removeLike(Film film, User user) {
        String sql = "DELETE FROM film_likes WHERE film_id=:film_id AND user_id=:user_id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", film.getId())
                .addValue("user_id", user.getId());
        jdbcTemplate.update(sql, parameterSource);
    }

    @Override
    public List<Film> getTopByLike(int count) {
        String sql = "SELECT f.id, f.name, f.description, f.release_date, f.mpa_id, " +
                            "f.duration, fg.genre_id, COUNT(fl.user_id) AS likes_num, " +
                            "g.name AS genre_name, m.NAME AS mpa_name " +
                     "FROM films AS f " +
                     "LEFT JOIN film_likes AS fl ON f.id=fl.film_id " +
                     "LEFT JOIN film_genre AS fg ON f.id=fg.film_id " +
                     "LEFT JOIN genres AS g ON fg.genre_id=g.id " +
                     "LEFT JOIN mpa AS m ON f.mpa_id=m.id " +
                     "GROUP BY f.id " +
                     "ORDER BY likes_num DESC " +
                     "LIMIT :count";

        SqlParameterSource parameterSource = new MapSqlParameterSource("count", count);
        List<Film> films = jdbcTemplate.query(sql, parameterSource, new FilmExtractor());

        return films;
    }

    private void addGenres(Long filmId, Set<Genre> genresId) {
        if (genresId.isEmpty()) {
            return;
        }

        StringBuilder sqlBuilder = new StringBuilder("INSERT INTO film_genre (film_id, genre_id) VALUES");
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("film_id", filmId);
        int i = 0;
        for (Genre genre : genresId) {
            final int genreId = genre.getId();
            sqlBuilder.append("(:film_id, :genre").append(i).append("),");
            parameterSource.addValue("genre"+i, genreId);
            i++;
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length()-1);
        String sql = sqlBuilder.toString();

        jdbcTemplate.update(sql, parameterSource);
    }

    private Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")))
                .genres(new TreeSet<>(Comparator.comparingInt(Genre::getId)))
                .build();
    }

    private class FilmExtractor implements ResultSetExtractor<List<Film>> {
        @Override
        public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, Film> films = new HashMap<>();

            while (rs.next()) {
                long filmId = rs.getLong("id");
                int genreId = rs.getInt("genre_id");
                if (films.containsKey(filmId)) {
                    films.get(filmId).getGenres().add(new Genre(genreId, rs.getString("genre_name")));
                } else {
                    Film newFilm = mapRow(rs, 0);
                    if (genreId > 0) {
                        newFilm.getGenres().add(new Genre(genreId,  rs.getString("genre_name")));
                    }
                    films.put(filmId, newFilm);
                }
            }

            return new ArrayList<>(films.values());
        }
    }
}
