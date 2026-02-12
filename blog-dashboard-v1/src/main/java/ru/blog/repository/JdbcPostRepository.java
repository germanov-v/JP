package ru.blog.repository;

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

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcPostRepository implements ru.blog.repository.base.PostRepository {

    //    @Autowired
//    private NamedParameterJdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcPostRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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


        return jdbcTemplate.query(sql, whereArgs, (resultSet, rowNumber) -> {
            var post = new Post();
            post.setId(resultSet.getLong("id"));
            post.setTitle(resultSet.getString("title"));
            post.setMainContent(resultSet.getString("main_text"));
            post.setImage(resultSet.getString("image"));
            post.setLikesCount(resultSet.getInt("likes_count"));
            post.setCommentsCount(resultSet.getInt("comment_count"));
            return post;
        });
    }

    @Override
    @Transactional
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

        final var tags = request.getTag();

        if (tags.length > 0) {

            params = new MapSqlParameterSource();
            params.addValue("post_id", postId); // .addValue("post_id", postId);
            StringBuilder sqlTagPost = new StringBuilder();
            for (int i = 0; i < tags.length; i++) {
                var key = "tag_value" + i;
                params.addValue(key, tags[i]);
                if (i != 0)
                    sqlTagPost.append(",");
              //  sqlTagPost.append(String.format("(:%s, :post_id)", key));
                sqlTagPost.append(":").append(key);

            }

            // language=sql
            final var sqlTags = String.format("""
                      WITH tags AS ( INSERT INTO posts.tags
                        (tag_value)
                      SELECT %s
                        ON CONFLICT (post_id, tag_id) DO NOTHING
                      RETURNING id)
                     INSERT INTO posts.posts_tags_tags(tag_id, post_id)
                     SELECT tags.id, :post_id
                    
                    """, sqlTagPost);

            jdbcTemplate.update(sqlTags, params);
        }
        return postId;
    }

    @Override
    public void update(EditRequestPostRequest request) {

    }

    @Override
    public Post findById(Long id) {
        return null;
    }
}
