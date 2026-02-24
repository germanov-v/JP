package ru.blog.dashboard.service;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.blog.dashboard.repository.base.PostRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileStorageService {

    private final PostRepository postRepository;

    public static final String UPLOAD_DIRECTORY = "uploads/";

    public FileStorageService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public void uploadImage(Long postId, MultipartFile file) {
        try {
            var uploadDir = Paths.get(UPLOAD_DIRECTORY);

            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            var filePath = uploadDir + "/" + file.getOriginalFilename();
            file.transferTo(Paths.get(filePath));

            postRepository.updateFile(postId, file.getOriginalFilename());

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }

    public Resource downloadImage(Long postId) {
        try {
            var fileName = postRepository.getFileName(postId);
            var filePath = Paths.get(UPLOAD_DIRECTORY + fileName);
            var content = Files.readAllBytes(filePath);
            return new ByteArrayResource(content);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
