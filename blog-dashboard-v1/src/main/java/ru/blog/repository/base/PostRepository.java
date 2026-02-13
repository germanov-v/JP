package ru.blog.repository.base;

import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.request.*;
import ru.blog.model.posts.response.CommentResponse;

import java.lang.management.LockInfo;
import java.util.List;

public interface PostRepository {

    public record FindResult(List<Post> posts, Integer count) {}
    FindResult find(ListPostRequest request);

    Long create(CreatePostRequest request);

    void update(EditRequestPostRequest request);

    void delete(Long id);

    Post findById(Long id);

    Integer addLike(Long postId);


    void updateFile(Long postId, String fileName);

    String getFileName(Long postId);

    List<CommentResponse> getComments(Long postId);

    CommentResponse getComment(Long postId, Long commentId);

    Long createComment(CreateCommentRequest request);

    void updateComment(EditCommentRequest request);


    void delete(Long postId, Long commentId);

}
