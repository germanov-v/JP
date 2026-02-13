package ru.blog.model.posts.request;

import ru.blog.model.posts.base.PostBodyBase;

public class CreateCommentRequest  {
   private Long postId;

   private String text;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
