package ru.blog.dashboard.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.blog.dashboard.model.PostMapper;
import ru.blog.dashboard.repository.JdbcPostRepository;
import ru.blog.dashboard.repository.base.PostRepository;
import ru.blog.dashboard.service.PostService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
@Import({PostService.class})
public class PostControllerToServiceMockRepositoryTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PostRepository postRepository;

    @MockitoBean
    PostMapper postMapper;

    @Test
    void addLike_checkCallsRepositoryOnce() throws Exception {
        when(postRepository.addLike(1L)).thenReturn(1);

        mockMvc.perform(
                   post("/api/posts/{id}/likes", 1L)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(1));
                //.andExpect(status())

       verify(postRepository, times(1)).addLike(1L);
    }

}
