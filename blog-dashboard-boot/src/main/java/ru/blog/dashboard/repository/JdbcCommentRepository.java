package ru.blog.dashboard.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.dashboard.model.posts.request.CreateCommentRequest;
import ru.blog.dashboard.model.posts.request.EditCommentRequest;
import ru.blog.dashboard.model.posts.response.CommentResponse;
import ru.blog.dashboard.repository.base.CommentRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCommentRepository implements CommentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CommentResponse> getComments(Long postId) {
        final String sql = """
                      SELECT 
                        id,
                        post_id,
                        text 
                      FROM posts.comments
                      WHERE post_id=:post_id
                """;

        return jdbcTemplate.query(sql,
                new MapSqlParameterSource().addValue("post_id", postId),
                this::GetCommentResponse);
    }

    @Override
    public CommentResponse getComment(Long postId, Long commentId) {
        final String sql = """
                      SELECT 
                        id,
                        post_id,
                        text 
                      FROM posts.comments
                      WHERE id=:id AND post_id=:post_id
                """;

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("post_id", postId)
                        .addValue("id", commentId),
                this::GetCommentResponse);
    }

    @Override
    public Long createComment(CreateCommentRequest request) {
        final String sql = """
                      INSERT INTO posts.comments(post_id, text)
                        VALUES (:post_id, :text) 
                      RETURNING id;
                """;

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("post_id", request.getPostId())
                        .addValue("text", request.getText()),
                Long.class);
    }


    @Override
    public void updateComment(EditCommentRequest request) {
        final String sql = """
                      UPDATE posts.comments
                      SET text=:text
                      WHERE 
                          post_id=:post_id
                          AND id=:id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource().addValue("post_id", request.getPostId())
                        .addValue("id", request.getId())
                        .addValue("text", request.getText()));
    }

    @Override
    public void delete(Long postId, Long commentId) {
        final String sql = """
                     DELETE FROM posts.comments
                
                      WHERE 
                          post_id=:post_id
                          AND id=:id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource().addValue("post_id", postId)
                        .addValue("id", commentId));
    }

    private CommentResponse GetCommentResponse(ResultSet st, int rowNumber) throws SQLException {
        var result = new CommentResponse();
        result.setId(st.getLong("id"));
        result.setText(st.getString("text"));
        result.setPostId(st.getLong("post_id"));
        return result;
    }
}
