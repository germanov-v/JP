package ru.blog.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.response.PostResponse;


@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "mainContent", target = "text")
    PostResponse toResponse(Post post);
}
