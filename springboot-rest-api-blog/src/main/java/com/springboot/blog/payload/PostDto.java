package com.springboot.blog.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        description = "It is a PostDto Model Information"
)
public class PostDto  {

    private Long id;

    @Schema(
            description = "Title of the Post"
    )
    @NotEmpty
    @Size(min = 2 , message = " title must have 3 character")
    private String title;

    @Schema(
            description = "Description of the Post"
    )
    @NotEmpty
    @Size(min = 10,message = "description must have 10 character ")
    private String description;

    @Schema(
            description = "Content of the Post "
    )
    @NotEmpty
    private String content;
    @Schema(
            description = "Comment Section of the Posts"
    )
    private Set<CommentDto> comments;


    @Schema(
            description = "CategoryId of the Posts"
    )
    private long categoryId;
}
