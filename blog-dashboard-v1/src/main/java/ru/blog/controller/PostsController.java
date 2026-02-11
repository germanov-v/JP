package ru.blog.controller;


import jdk.jshell.spi.ExecutionControl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.blog.model.posts.ListPostRequest;
import ru.blog.model.posts.ListPostResponse;
import ru.blog.model.response.PaginationResponse;

@RestController
@RequestMapping("/api/posts")
public class PostsController {


    // /api/posts?search=Lalala&pageNumber=1&pageSize=5
    @GetMapping
    @ResponseBody
    public ListPostResponse List(ListPostRequest request) {

        throw new UnsupportedOperationException("Not supported yet.");
       // return new Object();
    }
}
