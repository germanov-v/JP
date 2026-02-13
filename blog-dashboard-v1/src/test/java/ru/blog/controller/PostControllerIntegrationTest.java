package ru.blog.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.blog.configuration.DataSourceConfiguration;
import ru.blog.configuration.RestConfiguration;
import ru.blog.configuration.WebConfiguration;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.repository.base.PostRepository;
import ru.blog.service.PostService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringJUnitConfig(classes = {
        DataSourceConfiguration.class,
        WebConfiguration.class,
        RestConfiguration.class,
})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application.test.properties")
public class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper mapper;


    @Autowired
    private PostService postService;

    private MockMvc mockMvc;


    final private String mockTitle = "test title";
    final private String mockText = "test title";

    final private String mockTag1 = "test1";
    final private String mockTag2 = "test2";
    final private String mockTag3 = "test3";

    @BeforeEach
    void setupBeforeEachTest(){
         mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void cleanTest(){
         // TODO: truncate tables ?
        jdbcTemplate.update("""
                DELETE FROM posts.post_tags
                  WHERE tag_id IN 
                        (SELECT id FROM posts.tags 
                                   WHERE tag_value IN (:tags))
               """, new MapSqlParameterSource().addValue("tags",
                //   new String[]{mockTag1, mockTag2, mockTag3})
                List.of(mockTag1, mockTag2, mockTag3))
        );

        jdbcTemplate.update("""
                DELETE FROM posts.tags
                                   WHERE tag_value IN (:tags)
               """, new MapSqlParameterSource().addValue("tags",
                //   new String[]{mockTag1, mockTag2, mockTag3})
                List.of(mockTag1, mockTag2, mockTag3))
        );

        jdbcTemplate.update("""
                DELETE FROM posts.posts
                                   WHERE title IN (:title)
               """, new MapSqlParameterSource().addValue("title",
                //   new String[]{mockTag1, mockTag2, mockTag3})
                List.of(mockTitle))
        );
    }

    @Test
    void addPost_returnJson() throws Exception {
        var postRequest = createPostRequest();


        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }


    @Test
    void getPost_returnJson() throws Exception {

        var postRequest = createPostRequest();

        var post = postService.save(postRequest);


        mockMvc.perform(get("/api/posts/"+post.getId())
                     )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    @Test
    void getPostList_returnJson() throws Exception {

        createAndSavePost();
        createAndSavePost();
        createAndSavePost() ;




        mockMvc.perform(get("/api/posts?search="+mockTitle+"&pageNumber=1&pageSize=5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.[0].id").isNumber())
                .andExpect(jsonPath("$.posts").isArray())
        ;
    }

    private CreatePostRequest createPostRequest() {
        var postRequest = new CreatePostRequest();
        postRequest.setTag(new String[]{mockTag1,mockTag2, mockTag3});
        postRequest.setTitle(mockTitle);
        postRequest.setText(mockText);
        return postRequest;
    }

    private void savePostRequest(CreatePostRequest  createPostRequest) {
         postService.save(createPostRequest);
    }

    private void createAndSavePost() {
        postService.save(createPostRequest());
    }

}


