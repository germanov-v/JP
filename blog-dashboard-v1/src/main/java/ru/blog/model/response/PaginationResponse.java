package ru.blog.model.response;

public record PaginationResponse<TResult> (

    TResult result
){}
