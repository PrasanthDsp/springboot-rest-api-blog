package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Category;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    private CategoryRepository categoryRepository;

    private ModelMapper mapper;

    @Autowired
    public PostServiceImpl(PostRepository postRepository,ModelMapper mapper,CategoryRepository categoryRepository) {

        this.postRepository = postRepository;
        this.categoryRepository=categoryRepository;
        this.mapper = mapper;
    }

    /**
     * @param postDto
     * @return
     */

    @Override
    public PostDto createPost(PostDto postDto) {

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("category","id",postDto.getCategoryId()));


        //convert DTO to entity
        Post post = mapToEntity(postDto);
        post.setCategory(category);
        Post newPost = postRepository.save(post);

        // converting entity to DTO

        PostDto postResponse = mapToDto(newPost);
        return postResponse;
    }

    /**
     * @return
     */
    @Override
    public PostResponse getAllPosts(int postNo,int postSize,String sortBy,String sortType) {

        Sort sort = sortType.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(postNo,postSize, sort);
        Page<Post> posts  = postRepository.findAll(pageable);

        // get a content for page statement
        List<Post> listOfPost = posts.getContent();

        PostResponse postResponse = new PostResponse();


        List<PostDto> content = listOfPost.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    /**
     * @param id
     * @return
     */
    @Override
    public PostDto getPostById(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post","id",id));
        PostDto getPost = mapToDto(post);
        return getPost;
    }

    /**
     * @param postDto
     * @return
     */
    @Override
    public PostDto updatePost(PostDto postDto,long id) {

        // get post by id
        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post","id",id));

        Category category = categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("category","id",postDto.getCategoryId()));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        post.setCategory(category);
        Post updatePost = postRepository.save(post);

        return mapToDto(updatePost);
    }

    /**
     * @param id
     */
    @Override
    public void deletePost(long id) {

        Post post = postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("post","id",id));
        postRepository.delete(post);

    }

    /**
     * @param title
     */
    @Override
    public void deleteByTitle(String title) {
        Post post;
        try {
         post=   postRepository.findPostByTitle(title);
         postRepository.delete(post);
        } catch (Exception e) {
            throw new ResourceNotFoundException("post","id",title);
        }
    }

    /**
     * @param title
     * @return
     */
    @Override
    public PostDto getPostByTitle(String title) {
        Post post;
        try {
            post = postRepository.findPostByTitle(title);
        } catch (Exception e) {
            throw new ResourceNotFoundException("post","id",title);
        }

        return mapToDto(post);
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public List<PostDto> getPostsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("categeory", "id", categoryId));
        List<Post> posts = postRepository.findPostsByCategory_Id(categoryId);


        return posts.stream().map((post) ->
                mapToDto(post)).collect(Collectors.toList());
    }


    private PostDto mapToDto(Post newPost){

        PostDto postResponse = mapper.map(newPost,PostDto.class); // new way using mapper
//        PostDto postResponse = new PostDto();   old way of hardcoding to map EntityToDTO
//        postResponse.setId(newPost.getId());
//        postResponse.setTitle(newPost.getTitle());
//        postResponse.setDescription(newPost.getDescription());
//        postResponse.setContent(newPost.getContent());
        return postResponse;
    }

    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto,Post.class);
//        Post post = new Post();
//        post.setTitle(postDto.getTitle());
//        post.setDescription(postDto.getDescription());
//        post.setContent(postDto.getContent());
        return  post;
    }
}
