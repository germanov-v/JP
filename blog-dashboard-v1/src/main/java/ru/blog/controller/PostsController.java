package ru.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.blog.model.request.posts.ListPostRequest;
import ru.blog.model.response.PaginationResponse;

@RestController
@RequestMapping("/api/posts")
public class PostsController {


    // /api/posts?search=Lalala&pageNumber=1&pageSize=5
    @GetMapping
    @ResponseBody
    public PaginationResponse<ListPostRequest> List(ListPostRequest request) {

        return new PaginationResponse<>(request);
    }
}
