package ru.blog.controller;


import org.springframework.web.bind.annotation.*;
import ru.blog.model.posts.request.CreatePostRequest;
import ru.blog.model.posts.request.EditRequestPostRequest;
import ru.blog.model.posts.request.ListPostRequest;
import ru.blog.model.posts.response.ListPostResponse;
import ru.blog.model.posts.response.PostResponse;
import ru.blog.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostsController {

    private final PostService postService;

    public PostsController(PostService postService) {
        this.postService = postService;
    }

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

        return postService.save(request);
          // return new Object();
    }

    @PutMapping
    @ResponseBody
    public PostResponse Update(EditRequestPostRequest request) {

        throw new UnsupportedOperationException("Not supported yet.");
        // return new Object();
    }

}
