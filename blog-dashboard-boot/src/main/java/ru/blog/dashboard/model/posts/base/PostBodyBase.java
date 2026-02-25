package ru.blog.dashboard.model.posts.base;

public abstract class PostBodyBase {

    protected   String title;

    protected String text;

    protected String[] tag;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getTag() {
        return tag;
    }

    public void setTag(String[] tag) {
        this.tag = tag;
    }
}
