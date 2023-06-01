package com.zinchenko.admin.category;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.category.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryConvertor {

    public CategoryDto toDto(Category category) {
        return new CategoryDto()
                .setId(category.getCategoryId())
                .setName(category.getName());
    }
}
