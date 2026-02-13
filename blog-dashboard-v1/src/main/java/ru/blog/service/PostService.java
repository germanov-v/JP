package ru.blog.service;


import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.blog.model.PostMapper;
import ru.blog.model.posts.request.*;
import ru.blog.model.posts.response.CommentResponse;
import ru.blog.model.posts.response.ListPostResponse;
import ru.blog.model.posts.response.PostResponse;
import ru.blog.repository.base.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;


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

    public void UploadImage(Long id, MultipartFile file) {
        postRepository.delete(id);
    }

    public Resource DownloadImage(Long id) {
        throw new UnsupportedOperationException("Not supported yet.");
        //return  postRepository.delete(id);
    }

    public List<CommentResponse> getComments(long postId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CommentResponse getComment(long postId, long commentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public CommentResponse createComment(long postId, CreateCommentRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public CommentResponse updateComment(long postId, long commentId, EditCommentRequest request) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    public void deleteComment(long postId, long commentId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
