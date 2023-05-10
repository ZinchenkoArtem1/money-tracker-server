package com.zinchenko.admin.category;

import org.springframework.stereotype.Component;

@Component
public class CategoryConvertor {

    public CategoryDto toDto(Category category) {
        return new CategoryDto()
                .setId(category.getCategoryId())
                .setName(category.getName());
    }

    public Category fromDto(CategoryDto categoryDto) {
        return new Category()
                .setCategoryId(categoryDto.getId())
                .setName(categoryDto.getName());
    }
}
