package ru.blog.dashboard.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.dashboard.repository.base.FileRepository;


@Repository
public class JdbcFileRepository implements FileRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcFileRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void updateFile(Long postId, String fileName) {
        final String sql = """
                      UPDATE posts.posts
                      SET image=:image
                      WHERE id=:post_id
                """;

        jdbcTemplate.update(sql, new MapSqlParameterSource().addValue("image", fileName)
                .addValue("post_id", postId));
    }

    @Override
    public String getFileName(Long postId) {
        final String sql = """
                      SELECT image FROM posts.posts
                      WHERE id=:post_id
                """;

        return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource()
                .addValue("post_id", postId), String.class);
    }
}
