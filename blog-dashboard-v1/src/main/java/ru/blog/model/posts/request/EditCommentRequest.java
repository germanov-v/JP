package ru.blog.model.posts.request;

import ru.blog.model.posts.base.PostBodyBase;

public class EditCommentRequest extends CreateCommentRequest{

    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
