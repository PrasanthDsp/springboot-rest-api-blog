package com.springboot.blog.service.impl;

import ch.qos.logback.core.model.Model;
import com.springboot.blog.entity.Category;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;

    private ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * @param categoryDto
     * @return
     */
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {

        Category newCategory=mapToEntity(categoryDto);
        Category save = categoryRepository.save(newCategory);
        return mapToDto(save);
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public CategoryDto getCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("category", "id", categoryId)
        );

        return mapToDto(category);
    }

    /**
     * @return
     */
    @Override
    public List<CategoryDto> getAllCategory() {

        List<Category> allCategory = categoryRepository.findAll();

        return allCategory.stream().map((category) ->
            modelMapper.map(category,CategoryDto.class)).collect(Collectors.toList());
    }

    /**
     * @param categoryDto
     * @return
     */
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto,long categoryId) {

        if(categoryDto.getId()==categoryId){
            Category updateCategory = categoryRepository.findById(categoryId).orElseThrow(()->
                    new ResourceNotFoundException("category","id",categoryId));

            updateCategory.setName(categoryDto.getName());
            updateCategory.setDescription(categoryDto.getDescription());

            return mapToDto(updateCategory);
        }

        return null;
    }

    /**
     * @param categoryId
     */
    @Override
    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->
                new ResourceNotFoundException("category","id",categoryId));
        categoryRepository.delete(category);
    }

    private Category mapToEntity(CategoryDto categoryDto) {
        Category categoryReponse = modelMapper.map(categoryDto, Category.class);
        return categoryReponse;
    }

    private CategoryDto mapToDto(Category category) {
        CategoryDto map = modelMapper.map(category, CategoryDto.class);
        return map;
    }
}
