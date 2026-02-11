package ru.blog.controller;


import org.springframework.web.bind.annotation.*;
import ru.blog.model.posts.*;

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


    @GetMapping("/{id}")
    @ResponseBody
    public PostResponse Get(@PathVariable(name="id") Long id) {

        throw new UnsupportedOperationException("Not supported yet.");
        // return new Object();
    }

    @PostMapping
    @ResponseBody
    public PostResponse Add(CreatePostRequest request) {

        throw new UnsupportedOperationException("Not supported yet.");
        // return new Object();
    }

    @PutMapping
    @ResponseBody
    public PostResponse Add(EditRequestPostRequest request) {

        throw new UnsupportedOperationException("Not supported yet.");
        // return new Object();
    }

}
