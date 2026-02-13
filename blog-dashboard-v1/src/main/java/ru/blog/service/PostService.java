package ru.blog.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.blog.model.PostMapper;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.response.PostResponse;
import ru.blog.repository.base.PostRepository;

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
}
