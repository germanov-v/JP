package ru.blog.dashboard.service;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.blog.dashboard.model.PostMapper;
import ru.blog.dashboard.model.posts.db.Post;
import ru.blog.dashboard.model.posts.request.CreatePostRequest;
import ru.blog.dashboard.model.posts.response.PostResponse;
import ru.blog.dashboard.repository.base.PostRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@Tag("test")
@ExtendWith(MockitoExtension.class)
public class PostServiceUnitTest {


    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostService postService;


    @Test
    void create_shouldReturnPostResponse(){


        // arrange

        var request = new CreatePostRequest();

        var id = 101L;

        var post = new Post();
        PostResponse postResponse = new PostResponse();

        when(postRepository.create(request)).thenReturn(id);
        when(postRepository.findById(id)).thenReturn(post);
        when(postMapper.toResponse(post)).thenReturn(postResponse);


        // act

        var result = postService.create(request);

        // assert


        assertThat(result).isSameAs(postResponse);


        var inOrder = Mockito.inOrder(postRepository, postMapper);
        inOrder.verify(postRepository).create(request);
        inOrder.verify(postRepository)
                .findById(id);
        inOrder.verify(postMapper).toResponse(post);


       verifyNoMoreInteractions(postRepository, postMapper);

    }

}
