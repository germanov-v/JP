package ru.blog.repository.base;

import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.request.EditRequestPostRequest;
import ru.blog.model.posts.request.ListPostRequest;

import java.lang.management.LockInfo;
import java.util.List;

public interface PostRepository {

    public record FindResult(List<Post> posts, Integer count) {}
    FindResult find(ListPostRequest request);

    Long create(CreatePostRequest request);

    void update(EditRequestPostRequest request);

    void delete(Long id);

    Post findById(Long id);

    int addLike(Long postId);

}
