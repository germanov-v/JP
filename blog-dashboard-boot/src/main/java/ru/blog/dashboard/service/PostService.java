package ru.blog.dashboard.service;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.blog.dashboard.model.PostMapper;
import ru.blog.dashboard.model.posts.request.*;
import ru.blog.dashboard.model.posts.response.CommentResponse;
import ru.blog.dashboard.model.posts.response.ListPostResponse;
import ru.blog.dashboard.model.posts.response.PostResponse;
import ru.blog.dashboard.repository.base.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;

   // public static final String UPLOAD_DIRECTORY = "uploads/";

    private final PostMapper postMapper;

    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }


    @Transactional
    public PostResponse create(CreatePostRequest request) {
        var id = postRepository.create(request);
        var post = postRepository.findById(id);

        return postMapper.toResponse(post);
    }


    public PostResponse findById(long id) {

        var post = postRepository.findById(id);
        return postMapper.toResponse(post);
    }

    public ListPostResponse findByFilter(ListPostRequest request) {

        var postsResult = postRepository.find(request);

        var response = new ListPostResponse();
        response.setLastPage(Math.ceilDiv(postsResult.count(), request.getPageSize()));
        response.setPosts(postsResult.posts().stream()
                .map(postMapper::toResponse)
                .collect(Collectors.toList()));
        response.setHasNext(response.getLastPage() != request.getPageNumber());
        response.setHasPrev(request.getPageNumber() != 1);


        return response;
    }

    @Transactional
    public PostResponse update(EditRequestPostRequest request) {
        postRepository.update(request);
        var post = postRepository.findById(request.getId());

        return postMapper.toResponse(post);
    }


    @Transactional
    public void delete(Long id) {
        postRepository.delete(id);
    }

    public int addLike(Long postId) {
        return postRepository.addLike(postId);
    }


    public List<CommentResponse> getComments(Long postId) {
        return postRepository.getComments(postId);
    }

    public CommentResponse getComment(long postId, long commentId) {
        return postRepository.getComment(postId, commentId);
    }


    public CommentResponse createComment(long postId, CreateCommentRequest request) {
        var commentId = postRepository.createComment(request);
        return postRepository.getComment(postId, commentId);
    }


    public CommentResponse updateComment(long postId, long commentId, EditCommentRequest request) {
        postRepository.updateComment(request);
        return postRepository.getComment(postId, commentId);
    }


    public void deleteComment(long postId, long commentId) {
        postRepository.delete(postId, commentId);
    }

}
