package ru.blog.dashboard.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import ru.blog.dashboard.model.posts.db.Post;
import ru.blog.dashboard.model.posts.request.*;
import ru.blog.dashboard.model.posts.response.CommentResponse;
import ru.blog.dashboard.repository.base.PostRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class JdbcPostRepository implements PostRepository {

    //    @Autowired
//    private NamedParameterJdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate jdbcTemplate;
//    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(JdbcPostRepository.class);

    public JdbcPostRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        //   this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public FindResult find(ListPostRequest request) {

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
                SELECT pe.*, 
                       COALESCE( pc.count, 0) comment_count FROM posts_entities  pe
                LEFT JOIN count_comments pc ON pe.id=pc.post_id
                OFFSET :offset
                LIMIT :limit
                """, whereSql);

        // language=sql
        final var sqlCount = String.format("""
                  SELECT 
                      COUNT(*) count
                   FROM posts.posts
                   WHERE 1=1 
                   %s
                """, whereSql);

        var selectResult = jdbcTemplate.query(sql, whereArgs, this::GetResponse);
        var countResult = jdbcTemplate.queryForObject(sqlCount, whereArgs, Integer.class);


        return new FindResult(selectResult,
                countResult);
    }


    /**
     * m.b. unnest ?
     *
     * @param request
     * @return
     */
    @Override
    //@Transactional
    public Long create(CreatePostRequest request) {
        log.info("Transcat active: {}", TransactionSynchronizationManager.isActualTransactionActive());
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

            jdbcTemplate.batchUpdate(sqlInsertTag, paramBatch);

            final String sqlSelectTagIds = """
                       SELECT id
                       FROM posts.tags
                       WHERE tag_value IN (:tag_value)
                    """;


            final List<Long> tagIds = jdbcTemplate
                    .query(sqlSelectTagIds,
                            new MapSqlParameterSource().addValue("tag_value", tags)
                            //     .addValue("tag_value", request.getTag())
                            ,
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
                      -- ON CONFLICT (post_id, tag_id)
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
                        p.id,
                        title,
                        main_text,
                        image,
                        likes_count,
                        (
                          SELECT COUNT(*) FROM posts.comments pc WHERE pc.post_id=p.id
                        )  comment_count
                    FROM posts.posts p
                    LEFT JOIN posts.comments c
                      ON c.post_id = p.id
                    WHERE p.id=:id
                """;

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("id", id),
                this::GetResponse);

    }


    @Override
    public Integer addLike(Long postId) {
        final String sql = """
                      UPDATE posts.posts
                      SET likes_count=likes_count+1
                      WHERE  id=:id
                      RETURNING likes_count
                """;

        return jdbcTemplate.queryForObject(sql,
                new MapSqlParameterSource().addValue("id", postId), Integer.class);
    }

    @Override
    public void update(EditRequestPostRequest request) {
        final String sql = """
                      UPDATE posts.posts
                        SET
                            title=:title,
                            main_text=:main_text
                        WHERE  id=:id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource().addValue("id", request.getId())
                        .addValue("title", request.getTitle())
                        .addValue("main_text", request.getText()));
    }

    @Override
    public void delete(Long id) {
        final String sql = """
                      DELETE FROM posts.posts
                       WHERE  id=:id
                """;

        jdbcTemplate.update(sql,
                new MapSqlParameterSource()
                        .addValue("id", id));
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

    private CommentResponse GetCommentResponse(ResultSet st, int rowNumber) throws SQLException {
        var result = new CommentResponse();
        result.setId(st.getLong("id"));
        result.setText(st.getString("text"));
        result.setPostId(st.getLong("post_id"));
        return result;
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
