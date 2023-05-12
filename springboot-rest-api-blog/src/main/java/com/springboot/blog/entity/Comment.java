package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "body",nullable = false)
    private String body;
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "name",nullable = false)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)  // The fetchtype.lazy tells hibernate to only fetch the related entities from the database when you use this relationship
    @JoinColumn(name = "post_id")
    private Post post;
}
