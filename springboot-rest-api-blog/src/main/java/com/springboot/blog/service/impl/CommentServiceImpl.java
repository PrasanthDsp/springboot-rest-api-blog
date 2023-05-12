package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private PostRepository postRepository;

    private CommentRepository commentRepository;

    private ModelMapper modelMapper;

    @Autowired
    public CommentServiceImpl(PostRepository postRepository, CommentRepository commentRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper=modelMapper;
    }

    public CommentServiceImpl() {

    }

    /**
     * @param commentDto
     * @param postId
     * @return
     */
    @Override
    public CommentDto createComment(CommentDto commentDto, long postId) {
        Comment comment =  mapToEntity(commentDto);
        Post post= postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",postId));
        comment.setPost(post);
        Comment newComment = commentRepository.save(comment);

        return mapToDto(newComment);
    }

    /**
     * @return
     */
    @Override
    public List<CommentDto> getAllComments(long postId) {
        postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",postId));
        List<Comment> comments=commentRepository.findCommentsByPostId( postId);
        return comments.stream().map(comment -> mapToDto(comment)).collect(Collectors.toList());
    }

    /**
     * @param postId
     * @param commentId
     * @return
     */
    @Override
    public CommentDto getCommentById(long postId, long commentId) {
         Post post =postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",postId));

         Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",commentId));

         if(!comment.getPost().getId().equals(post.getId())){
             throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to the post");
         }


        return mapToDto(comment);
    }

    /**
     * @param postId
     * @param commentId
     * @param commentDto
     * @return
     */
    @Override
    public CommentDto updateCommentById(long postId, long commentId, CommentDto commentDto) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post Id");
        }

        comment.setBody(commentDto.getBody());
        comment.setEmail(commentDto.getEmail());
        comment.setName(commentDto.getName());
        Comment updateComment=commentRepository.save(comment);
        return mapToDto(updateComment);
    }

    /**
     * @param postId
     * @param commentId
     */
    @Override
    public void deleteCommentById(long postId, long commentId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",postId));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Post","Id",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST,"Comment does not belong to post Id");
        }

        commentRepository.delete(comment);
    }


    private CommentDto mapToDto(Comment newComment){

        CommentDto commentResponse = modelMapper.map(newComment,CommentDto.class); // new way using mapper
//        PostDto postResponse = new PostDto();   old way of hardcoding to map EntityToDTO
//        postResponse.setId(newPost.getId());
//        postResponse.setTitle(newPost.getTitle());
//        postResponse.setDescription(newPost.getDescription());
//        postResponse.setContent(newPost.getContent());
        return commentResponse;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = modelMapper.map(commentDto,Comment.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return  comment;
    }
}
