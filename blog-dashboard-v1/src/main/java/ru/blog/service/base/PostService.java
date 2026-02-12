package ru.blog.service.base;

import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.response.PostResponse;

public interface PostService {
    public PostResponse save(CreatePostRequest request);
}