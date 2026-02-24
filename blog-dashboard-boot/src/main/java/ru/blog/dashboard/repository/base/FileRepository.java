package ru.blog.dashboard.repository.base;

public interface FileRepository {
    void updateFile(Long postId, String fileName);

    String getFileName(Long postId);
}
