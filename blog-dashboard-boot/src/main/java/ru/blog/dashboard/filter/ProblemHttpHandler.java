package ru.blog.dashboard.filter;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ProblemHttpHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail notFound404(NoResourceFoundException ex,
                                     HttpServletRequest request) {
        var response = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        response.setDetail(ex.getMessage());
        response.setTitle("Source not found");
        response.setProperty("path", request.getRequestURI());
        response.setProperty("status_code", "not_found");

        return response;
    }


    @ExceptionHandler(Exception.class)
    public ProblemDetail allErrors(Exception ex, HttpServletRequest request) {
        var response = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        response.setDetail(ex.getMessage());
        response.setTitle("Internal server error");
        response.setProperty("path", request.getRequestURI());
        response.setProperty("status_code", "internal_server_error");


        return response;
    }


}
