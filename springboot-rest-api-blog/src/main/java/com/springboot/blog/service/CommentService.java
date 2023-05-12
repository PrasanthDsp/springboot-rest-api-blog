package com.springboot.blog.service;

import com.springboot.blog.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(CommentDto commentDto,long postId);

    List<CommentDto> getAllComments(long postId);

    CommentDto getCommentById(long postId,long commentId);

    CommentDto updateCommentById(long postId,long commentId, CommentDto commentDto);

    void deleteCommentById(long postId,long commentId);
}
