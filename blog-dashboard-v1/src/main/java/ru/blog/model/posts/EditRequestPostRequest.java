package ru.blog.model.posts;

import ru.blog.model.posts.base.PostBodyBase;

public class EditRequestPostRequest  extends PostBodyBase {
    public Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
