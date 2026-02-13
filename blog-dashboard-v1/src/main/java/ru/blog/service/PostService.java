package ru.blog.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.blog.model.PostMapper;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.request.ListPostRequest;
import ru.blog.model.posts.response.ListPostResponse;
import ru.blog.model.posts.response.PostResponse;
import ru.blog.repository.base.PostRepository;

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
    public PostResponse save(CreatePostRequest request) {
        var id = postRepository.save(request);
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
        response.setHasNext(response.getLastPage()!=request.getPageNumber());
        response.setHasPrev(request.getPageNumber()!=1);


        return response;
    }
}
