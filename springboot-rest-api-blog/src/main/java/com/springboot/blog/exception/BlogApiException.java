package com.springboot.blog.exception;

import org.springframework.http.HttpStatus;

public class BlogApiException extends RuntimeException {


     //  we throw this Exception whenever we write some business logic or validate request parameter

    private HttpStatus status;
    private String message;

    public BlogApiException(HttpStatus status,String message){
        this.status=status;
        this.message=message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
