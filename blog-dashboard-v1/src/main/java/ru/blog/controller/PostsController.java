package ru.blog.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.blog.model.posts.request.*;
import ru.blog.model.posts.response.CommentResponse;
import ru.blog.model.posts.response.ListPostResponse;
import ru.blog.model.posts.response.PostResponse;
import ru.blog.service.PostService;

import java.util.List;

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
        return postService.findByFilter(request);
        //return postService.findById(id);
        // throw new UnsupportedOperationException("Not supported yet.");
        // return new Object();
    }


    @GetMapping("/{id}")
    @ResponseBody
    public PostResponse Get(@PathVariable(name = "id") Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseBody
    public PostResponse Add(@RequestBody CreatePostRequest request) {

        var result = postService.create(request);
        return result;
        // return new Object();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public PostResponse Update(@PathVariable(name = "id") Long id, @RequestBody EditRequestPostRequest request) {

        return postService.update(request);
    }

    @DeleteMapping("/{id}")
    public void Delete(@PathVariable(name = "id") Long id) {
        postService.delete(id);
    }


    @PostMapping("/{id}/likes")
    @ResponseBody
    public int Like(@PathVariable(name = "id") Long id) {
        return postService.addLike(id);
    }



     // NOT TESTED
    // ================================







    @PostMapping("/{id}/image")
    public void UploadImage(@PathVariable(name = "id") Long id, @RequestParam("file") MultipartFile file) {
        postService.UploadImage(id, file);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> UploadImage(@PathVariable(name = "id") Long id) {
       var resource = postService.DownloadImage(id);

       return ResponseEntity.ok()
               .contentType(MediaType.APPLICATION_OCTET_STREAM)
               //.contentType(MediaType.IMAGE_JPEG)
               .body(resource);
    }


    @GetMapping("/{id}/comments")
    @ResponseBody
    public List<CommentResponse> GetComments(@PathVariable(name = "id") Long id) {
        return postService.getComments(id);
    }

    @GetMapping("/{id}/comments/{commentId}")
    @ResponseBody
    public CommentResponse GetComment(@PathVariable(name = "id") Long id,
                                            @PathVariable(name = "commentId") Long commentId) {
        return postService.getComment(id,commentId);
    }

    @PostMapping("/{id}/comments")
    @ResponseBody
    public CommentResponse SetComment(@PathVariable(name = "id") Long id,
                                      @RequestBody CreateCommentRequest request) {
        return postService.createComment(id,request);
    }

    @PutMapping("/{id}/comments/{commentId}")
    @ResponseBody
    public CommentResponse GetComment(@PathVariable(name = "id") Long id,
                                      @PathVariable(name = "commentId") Long commentId,
                                      @RequestBody EditCommentRequest request) {
        return postService.updateComment(id,commentId, request);
    }


    @DeleteMapping("/{id}/comments/{commentId}")
    public void Delete(@PathVariable(name = "id") Long id,
                       @PathVariable(name = "commentId") Long commentId) {
        postService.deleteComment(id,commentId);
    }
}
