package ru.blog.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    private ObjectMapper mapper;

    private MockMvc mockMvc;


    @BeforeEach
    void setupBeforeEachTest(){
         mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void addUser_returnJson() throws Exception {
        var postRequest = new CreatePostRequest()

                ;
        postRequest.setTag(new String[]{"test1", "test2", "test3"});
        postRequest.setTitle("test title");
        postRequest.setText("test text");


        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
        ;
    }

}


