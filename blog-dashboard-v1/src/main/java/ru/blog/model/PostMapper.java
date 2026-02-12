package ru.blog.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.blog.model.posts.db.Post;
import ru.blog.model.posts.response.PostResponse;


@Mapper(componentModel = "sping")
public interface PostMapper {

    @Mapping(source = "mainText", target = "text")
    PostResponse toResponse(Post post);
}
