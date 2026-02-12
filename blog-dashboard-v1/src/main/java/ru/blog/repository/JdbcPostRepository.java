package ru.blog.repository;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.request.EditRequestPostRequest;
import ru.blog.model.posts.request.ListPostRequest;
import ru.blog.model.posts.response.PostResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcPostRepository implements ru.blog.repository.base.PostRepository {

    //    @Autowired
//    private NamedParameterJdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcPostRepository(NamedParameterJdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Post> find(ListPostRequest request) {

        var whereSql = new StringBuilder();
        var whereArgs = new MapSqlParameterSource();

        var page = (request.getPageNumber() == null || request.getPageNumber() < 1) ? 1 : request.getPageNumber();
        var size = (request.getPageSize() == null || request.getPageSize() < 1) ? 30 : request.getPageSize();
        var offset = (page - 1) * size;

        whereArgs.addValue("offset", offset);
        whereArgs.addValue("limit", size);


        var search = request.getSearch();
        if (search != null && !search.trim().isEmpty()) {
            whereSql.append("""
                      AND (
                       title LIKE CONCAT('%', :search, '%')
                        OR  
                       main_text LIKE CONCAT('%', :search, '%')   
                          )
                    """);
            whereArgs.addValue("search", search.trim());
        }

        // language=sql
        final var sql = String.format("""
                WITH posts_entities AS (
                  SELECT 
                       id,
                       title,
                       main_text,
                       image,
                       likes_count
                   FROM posts.posts
                   WHERE 1=1
                   %s
                ),
                count_comments AS (
                   SELECT post_id, COUNT(*) count FROM posts.comments pc
                   GROUP BY post_id
                )
                SELECT posts.*, 
                       COALESCE( pc.count, 0) comment_count FROM posts_entities  pe
                LEFT JOIN count_comments pc ON pe.id=pc.post_id
                OFFSET :offset
                LIMIT :limit
                """, whereSql);


        return jdbcTemplate.query(sql, whereArgs, this::GetResponse);
    }


    /**
     * m.b. unnest ?
     * @param request
     * @return
     */
    @Override
    //@Transactional
    public Long save(CreatePostRequest request) {

        // language=sql
        final var sqlPosts = """
                  INSERT INTO posts.posts
                  (title, main_text)
                  VALUES (:title, :main_text)
                  RETURNING id
                """;


        var params = new MapSqlParameterSource();
        params.addValue("title", request.getTitle());
        params.addValue("main_text", request.getText());


        var postId = jdbcTemplate.queryForObject(sqlPosts, params, Long.class);//.getLong("id");

        final var tagsRaw = request.getTag();

        if (tagsRaw.length > 0) {

            final List<String> tags = Arrays.stream(tagsRaw)
                    .filter(Objects::nonNull)
                    //.map(str->str.trim())
                    .map(String::trim)
                    .filter(str -> !str.isEmpty())
                    .distinct()
                    .toList();

            if (tags.isEmpty()) {
                return postId;
            }

            // language=sql
            final String sqlInsertTag = """
                          INSERT INTO posts.tags(tag_value)
                          VALUES (:tag_value)
                          ON CONFLICT (tag_value) 
                          DO NOTHING
                    """;

            final MapSqlParameterSource[] paramBatch =
                    tags.stream()
                            .map(p -> new MapSqlParameterSource().addValue("tag_value", p))
                            .toArray(MapSqlParameterSource[]::new);

            namedParameterJdbcTemplate.batchUpdate(sqlInsertTag, paramBatch);

            final String sqlSelectTagIds = """
                       SELECT id
                       FROM posts.tags
                       WHERE tag_value IN (:tag_value)
                    """;


            final List<Long> tagIds = namedParameterJdbcTemplate
                    .query(sqlSelectTagIds,
                            new MapSqlParameterSource().addValue("tags", tags),
                            (rs, i) -> rs.getLong("id")
                    );

            if (tagIds.isEmpty()) {
                 throw new UnsupportedOperationException();
                 // return postId;
            }
            // language=sql
            final String sqlInsertPostTags = """
                       INSERT INTO posts.post_tags(post_id, tag_id)
                       VALUES (:post_id, :tag_id)
                       ON CONFLICT (post_id, tag_id)
                    """;

            final var paramBatchTags = tagIds.stream()
                            .map(p -> new MapSqlParameterSource().addValue("post_id", postId)
                                    .addValue("tag_id", p))
                    .toArray(MapSqlParameterSource[]::new);

            jdbcTemplate.batchUpdate(sqlInsertPostTags, paramBatchTags);
        }
        return postId;
    }

    @Override
    public void update(EditRequestPostRequest request) {

    }

    @Override
    public Post findById(Long id) {
        // language=sql
//       final var sql = """
//                      SELECT
//                       id,
//                       title,
//                       main_text,
//                       image,
//                       likes_count,
//                       COALESCE(COUNT(c.id), 0) as comment_count
//                   FROM posts.posts p
//                   LEFT JOIN posts.comments c
//                     ON p.post_id = c.id
//                   WHERE id=:id
//                   GROUP BY
//                       p.id,
//                       p.title,
//                       p.main_text,
//                       p.image,
//                       p.likes_count
//               """;

               final var sql = """
                      SELECT 
                       id,
                       title,
                       main_text,
                       image,
                       likes_count,
                       (
                         SELECT (COUNT(*) FROM posts.comments WHERE post_id=p.id)
                       ) comment_count
                   FROM posts.posts p
                   LEFT JOIN posts.comments c
                     ON p.post_id = c.id
                   WHERE id=:id
               """;

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("id", id),
                this::GetResponse);

    }

    private Post GetResponse(ResultSet resultSet, int rowNumber) throws SQLException {
        var post = new Post();
        post.setId(resultSet.getLong("id"));
        post.setTitle(resultSet.getString("title"));
        post.setMainContent(resultSet.getString("main_text"));
        post.setImage(resultSet.getString("image"));
        post.setLikesCount(resultSet.getInt("likes_count"));
        post.setCommentsCount(resultSet.getInt("comment_count"));
        return post;
    }

    @Transactional
    public Long saveV1(CreatePostRequest request) {

        // language=sql
        final var sqlPosts = """
                  INSERT INTO posts.posts
                  (title, main_text)
                  VALUES (:title, :main_text)
                  RETURNING id
                """;


        var params = new MapSqlParameterSource();
        params.addValue("title", request.getTitle());
        params.addValue("main_text", request.getText());


        var postId = jdbcTemplate.queryForObject(sqlPosts, params, Long.class);//.getLong("id");

        final var tags = request.getTag();

        if (tags.length > 0) {

            params = new MapSqlParameterSource();
            params.addValue("post_id", postId); // .addValue("post_id", postId);
            StringBuilder sqlTagPost = new StringBuilder();
            StringBuilder values = new StringBuilder();
            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                if (tag == null || tag.trim().isEmpty()) continue;
                String key = "tag_value" + i;
                params.addValue(key, tag.trim());

                if (values.length() > 0) values.append(", ");
                values.append("(:").append(key).append(")");
            }


            // language=sql
            final var sqlTags = String.format("""
                    WITH input_tags AS (
                        SELECT DISTINCT v.tag_value
                        FROM (VALUES %s) AS v(tag_value)
                    ),
                    ins_tags AS (
                        INSERT INTO posts.tags(tag_value)
                        SELECT tag_value
                        FROM input_tags
                        ON CONFLICT (tag_value) DO NOTHING
                        RETURNING id, tag_value
                    ),
                    tag_ids AS (
                        SELECT t.id
                        FROM posts.tags t
                        JOIN input_tags it ON it.tag_value = t.tag_value
                    )
                    INSERT INTO posts.post_tags(post_id, tag_id)
                    SELECT :post_id, id
                    FROM tag_ids
                    ON CONFLICT DO NOTHING
                    """, values);


            jdbcTemplate.update(sqlTags, params);
        }
        return postId;
    }

}
