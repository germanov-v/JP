package ru.blog.dashboard.controller;


import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.blog.dashboard.model.posts.request.*;
import ru.blog.dashboard.model.posts.response.CommentResponse;
import ru.blog.dashboard.model.posts.response.ListPostResponse;
import ru.blog.dashboard.model.posts.response.PostResponse;
import ru.blog.dashboard.service.FileStorageService;
import ru.blog.dashboard.service.PostService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    private final FileStorageService fileStorageService;

    public PostController(PostService postService, FileStorageService fileStorageService) {
        this.postService = postService;
        this.fileStorageService = fileStorageService;
    }

    // /api/posts?search=Lalala&pageNumber=1&pageSize=5
    @GetMapping
    @ResponseBody
    public ListPostResponse list(ListPostRequest request) {
        return postService.findByFilter(request);
    }


    @GetMapping("/{id}")
    @ResponseBody
    public PostResponse get(@PathVariable(name = "id") Long id) {
        return postService.findById(id);
    }

    @PostMapping
    @ResponseBody
    public PostResponse add(@RequestBody CreatePostRequest request) {
        var result = postService.create(request);
        return result;
    }

    @PutMapping("/{id}")
    @ResponseBody
    public PostResponse update(@PathVariable(name = "id") Long id, @RequestBody EditRequestPostRequest request) {

        return postService.update(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        postService.delete(id);
    }


    @PostMapping("/{id}/likes")
    @ResponseBody
    public int like(@PathVariable(name = "id") Long id) {
        return postService.addLike(id);
    }

    @PostMapping("/{id}/image")
    public void uploadImage(@PathVariable(name = "id") Long id, @RequestParam("file") MultipartFile file) {
        fileStorageService.uploadImage(id, file);
    }


    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> uploadImage(@PathVariable(name = "id") Long id) {
        var resource = fileStorageService.downloadImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                //.contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }


    @GetMapping("/{id}/comments")
    @ResponseBody
    public List<CommentResponse> getComments(@PathVariable(name = "id") Long id) {
        return postService.getComments(id);
    }

    @PostMapping("/{id}/comments")
    @ResponseBody
    public CommentResponse setComment(@PathVariable(name = "id") Long id,
                                      @RequestBody CreateCommentRequest request) {
        return postService.createComment(id,request);
    }


    @GetMapping("/{id}/comments/{commentId}")
    @ResponseBody
    public CommentResponse getComment(@PathVariable(name = "id") Long id,
                                      @PathVariable(name = "commentId") Long commentId) {
        return postService.getComment(id,commentId);
    }

    @PutMapping("/{id}/comments/{commentId}")
    @ResponseBody
    public CommentResponse updateComment(@PathVariable(name = "id") Long id,
                                         @PathVariable(name = "commentId") Long commentId,
                                         @RequestBody EditCommentRequest request) {
        return postService.updateComment(id,commentId, request);
    }


    @DeleteMapping("/{id}/comments/{commentId}")
    public void delete(@PathVariable(name = "id") Long id,
                       @PathVariable(name = "commentId") Long commentId) {
        postService.deleteComment(id,commentId);
    }

    // NOT TESTED
    // ================================






}