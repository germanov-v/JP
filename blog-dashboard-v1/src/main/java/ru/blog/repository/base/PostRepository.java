package ru.blog.repository.base;

import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.request.EditRequestPostRequest;
import ru.blog.model.posts.request.ListPostRequest;

import java.util.List;

public interface PostRepository {

    List<Post> find(ListPostRequest request);

    Long save(CreatePostRequest request);

    void update(EditRequestPostRequest request);

    Post findById(Long id);

}
