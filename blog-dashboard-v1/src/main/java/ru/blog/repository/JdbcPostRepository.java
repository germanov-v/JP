package ru.blog.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.request.EditRequestPostRequest;
import ru.blog.model.posts.request.ListPostRequest;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcPostRepository implements ru.blog.repository.base.PostRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Post> find(ListPostRequest request) {

        var whereSql = new StringBuilder();
        List<Object> whereArgs = new ArrayList<>();
        if(request.getSearch() != null && !request.getSearch().trim().isEmpty()) {
            whereSql.append("""
                      AND (
                       title LIKE CONCAT('%', :search, '%')
                        OR  
                       main_text LIKE CONCAT('%', :search, '%')   
                          )
                    """ );
            whereArgs.add(request.getSearch());
        }

        // language=sql
         var sql = String.format("""
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
          SELECT posts.*, count_comments.count FROM posts_entities  pe
          LEFT JOIN count_comments pc ON pe.id=pc.post_id
          OFFSET %d
          LIMIT %d
          """, whereSql, (request.getPageNumber()-1)*request.getPageSize() ,request.getPageSize());


//        var result = jdbcTemplate.query(sql, whereArgs, (resultSet, rowNumber)->{
//
//        });


        return List.of();
    }

    @Override
    public Long save(CreatePostRequest request) {
        return 0L;
    }

    @Override
    public void update(EditRequestPostRequest request) {

    }

    @Override
    public Post findById(Long id) {
        return null;
    }
}
