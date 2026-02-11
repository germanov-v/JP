package ru.blog.model.responses;

public record PaginationResponse<TResult> (

    TResult result
){}
