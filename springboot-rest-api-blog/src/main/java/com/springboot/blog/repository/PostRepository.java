package com.springboot.blog.repository;

import com.springboot.blog.entity.Post;
import com.springboot.blog.payload.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    void deletePostByTitle(String title);
    Post findPostByTitle(String title);
    List<Post> findPostsByCategory_Id(long id);
}

