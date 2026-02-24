package ru.blog.dashboard.repository.base;

import ru.blog.dashboard.model.posts.request.CreateCommentRequest;
import ru.blog.dashboard.model.posts.request.EditCommentRequest;
import ru.blog.dashboard.model.posts.response.CommentResponse;

import java.util.List;

public interface CommentRepository {


    List<CommentResponse> getComments(Long postId);

    CommentResponse getComment(Long postId, Long commentId);

    Long createComment(CreateCommentRequest request);

    void updateComment(EditCommentRequest request);


    void delete(Long postId, Long commentId);
}
