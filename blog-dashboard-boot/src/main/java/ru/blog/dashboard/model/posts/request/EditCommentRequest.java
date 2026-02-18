package ru.blog.dashboard.model.posts.request;

public class EditCommentRequest extends CreateCommentRequest{

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
