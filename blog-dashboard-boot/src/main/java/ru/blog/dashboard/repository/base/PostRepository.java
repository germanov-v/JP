package ru.blog.dashboard.repository.base;


import ru.blog.dashboard.model.posts.db.Post;
import ru.blog.dashboard.model.posts.request.*;
import ru.blog.dashboard.model.posts.response.CommentResponse;

import java.util.List;

public interface PostRepository {

    public record FindResult(List<Post> posts, Integer count) {}

    FindResult find(ListPostRequest request);

    Long create(CreatePostRequest request);

    void update(EditRequestPostRequest request);

    void delete(Long id);

    Post findById(Long id);

    Integer addLike(Long postId);






}
