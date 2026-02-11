package ru.blog.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.blog.model.responses.PaginationResponse;

@Controller("/api/posts")
public class PostsController {



    @GetMapping
    @ResponseBody
    public PaginationResponse<String> List(){

        return new PaginationResponse<String>("test");
    }
}
