package ru.blog.dashboard.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.blog.dashboard.model.posts.request.CreateCommentRequest;
import ru.blog.dashboard.model.posts.request.CreatePostRequest;
import ru.blog.dashboard.model.posts.request.EditCommentRequest;
import ru.blog.dashboard.model.posts.request.EditRequestPostRequest;
import ru.blog.dashboard.service.PostService;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // полный контекст
        //(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
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

    @Autowired
    private MockMvc mockMvc;


    final private String mockTitle = "test title";
    final private String mockText = "test title";

    final private String mockTag1 = "test1";
    final private String mockTag2 = "test2";
    final private String mockTag3 = "test3";

    @BeforeEach
    void setupBeforeEachTest() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void cleanTest() {
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
                List.of(mockTitle, mockTag1))
        );

        jdbcTemplate.update("""
                 DELETE FROM posts.comments
                                    WHERE text IN (:text)
                """, new MapSqlParameterSource().addValue("text",
                //   new String[]{mockTag1, mockTag2, mockTag3})
                List.of(mockTag1, mockTag2))
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

        var post = postService.create(postRequest);


        mockMvc.perform(get("/api/posts/" + post.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    @Test
    void getPostList_returnJson() throws Exception {

        createAndSavePost();
        createAndSavePost();
        createAndSavePost();


        mockMvc.perform(get("/api/posts?search=" + mockTitle + "&pageNumber=1&pageSize=5")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts.[0].id").isNumber())
                .andExpect(jsonPath("$.posts").isArray())
        ;
    }

    @Test
    void updatePost_returnJson() throws Exception {
        var postRequest = createPostRequest();

        var post = postService.create(postRequest);

        var editPostRequest = new EditRequestPostRequest();
        editPostRequest.setId(post.getId());
        editPostRequest.setTitle(mockTag1);
        editPostRequest.setText(mockTag2);

        mockMvc.perform(put("/api/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(editPostRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    @Test
    void deletePost() throws Exception {
        var postRequest = createPostRequest();

        var post = postService.create(postRequest);

        mockMvc.perform(delete("/api/posts/" + post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
        ;
    }

    @Test
    void likePost() throws Exception {
        var postRequest = createPostRequest();

        var post = postService.create(postRequest);

        var result = mockMvc.perform(post("/api/posts/" + post.getId() + "/likes")
                        //        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var value = Integer.parseInt(result);
        assert (value == 1);
    }


    @Test
    void uploadImage() throws Exception {
        var postRequest = createPostRequest();
        var post = postService.create(postRequest);


        var bytes = mockTag1.getBytes();
        var file = new MockMultipartFile("file", "a.png", "image/png", bytes);


        mockMvc.perform(
                        multipart("/api/posts/{id}/image", post.getId()).file(file)
                )
                .andExpect(status().isOk());
        var fileSaved = Paths.get(PostService.UPLOAD_DIRECTORY + file.getOriginalFilename());
        assert (Files.exists(fileSaved));

        mockMvc.perform(
                        get("/api/posts/{id}/image", post.getId())
                )
                .andExpect(status().isOk());

        Files.delete(fileSaved);
    }


    @Test
    void getCommentList_returnJson() throws Exception {

       var result = createAndSavePostComment(3);


        mockMvc.perform(get("/api/posts/{id}/comments",result.postId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
        ;
    }


    @Test
    void addComment_returnJson() throws Exception {
        var postId = createAndSavePost();
        var comment = createCommentRequest(postId);

        mockMvc.perform(post("/api/posts/{id}/comments",comment.getPostId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

    @Test
    void updateComment_returnJson() throws Exception {

        var comment = createAndSavePostComment(1);

        var commentUpdateRequest = new EditCommentRequest();
        commentUpdateRequest.setId(comment.commentIds.getFirst());
        commentUpdateRequest.setText(mockTag2);
        commentUpdateRequest.setPostId(comment.postId);

        mockMvc.perform(put("/api/posts/{id}/comments/{commentId}",
                        comment.postId,
                        comment.commentIds.getFirst()
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value(mockTag2))
        ;
    }


    @Test
    void deleteComment() throws Exception {
        var comment = createAndSavePostComment(1);

        mockMvc.perform(delete("/api/posts/{id}/comments/{commentId}",
                        comment.postId,
                        comment.commentIds.getFirst()
                )
                        )
                .andExpect(status().isOk())
        ;
    }

    private CreatePostRequest createPostRequest() {
        var postRequest = new CreatePostRequest();
        postRequest.setTag(new String[]{mockTag1, mockTag2, mockTag3});
        postRequest.setTitle(mockTitle);
        postRequest.setText(mockText);
        return postRequest;
    }

    private CreateCommentRequest createCommentRequest(Long postId) {
        var request = new CreateCommentRequest();
        request.setPostId(postId);
        request.setText(mockText);
        return request;
    }

    private void savePostRequest(CreatePostRequest createPostRequest) {
        postService.create(createPostRequest);
    }

    private Long createAndSavePost() {
       return   postService.create(createPostRequest()).getId();
    }


    record CommentPostResult(Long postId, List<Long> commentIds) {
    }

    private CommentPostResult createAndSavePostComment(int count) {
        var post = postService.create(createPostRequest());
        List<Long> commentIds = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            var comment = createCommentRequest(post.getId());
            var commentResult = postService.createComment(post.getId(), comment);
            commentIds.add(commentResult.getId());
        }
        return new CommentPostResult(post.getId(), commentIds);
    }

}


