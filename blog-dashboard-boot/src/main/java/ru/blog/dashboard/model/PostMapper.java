package ru.blog.dashboard.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.blog.dashboard.model.posts.db.Post;
import ru.blog.dashboard.model.posts.response.PostResponse;


@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "mainContent", target = "text")
    PostResponse toResponse(Post post);
}
