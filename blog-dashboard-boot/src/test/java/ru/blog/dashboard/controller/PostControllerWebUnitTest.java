package ru.blog.dashboard.controller;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockReset;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.blog.dashboard.model.posts.request.ListPostRequest;
import ru.blog.dashboard.model.posts.response.ListPostResponse;
import ru.blog.dashboard.service.FileStorageService;
import ru.blog.dashboard.service.PostService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;


@WebMvcTest(controllers = PostController.class)
@Tag("test")
public class PostControllerWebUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean(reset = MockReset.BEFORE)
    PostService postService;

    @MockitoBean(reset = MockReset.BEFORE)
    FileStorageService fileStorageService;


    @Test
    void listBindsQueryParams() throws Exception {
        when(postService.findByFilter(any())).thenReturn(new ListPostResponse());

        mockMvc.perform(get("/api/posts")
                        .param("search", "test")
                        .param("pageNumber", "1")
                        .param("pageSize", "10")
                )
                .andExpect(status().isOk());

        ArgumentCaptor<ListPostRequest> captor = ArgumentCaptor.forClass(ListPostRequest.class);
        verify(postService).findByFilter(captor.capture());

        var request = captor.getValue();
        assertThat(request.getSearch()).isEqualTo("test");

    }

}
