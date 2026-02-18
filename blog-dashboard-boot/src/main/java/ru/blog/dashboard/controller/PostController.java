package ru.blog.dashboard.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.blog.dashboard.model.posts.request.ListPostRequest;
import ru.blog.dashboard.model.posts.response.ListPostResponse;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @GetMapping
    @ResponseBody
    public ListPostResponse List(ListPostRequest request){
        ListPostResponse response = new ListPostResponse();


        return  response;
    }
}
