package com.zinchenko.admin.category;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.category.dto.CategoryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CategoryConvertorTest extends RandomGenerator {

    private CategoryConvertor categoryConvertor;

    @BeforeEach
    void setUp() {
        categoryConvertor = new CategoryConvertor();
    }

    @Test
    void toDtoTest() {
        Category category = random(Category.class);

        CategoryDto categoryDto = categoryConvertor.toDto(category);

        assertEquals(category.getCategoryId(), categoryDto.getId());
        assertEquals(category.getName(), categoryDto.getName());
    }

    @Test
    void fromDtoTest() {
        CategoryDto categoryDto = random(CategoryDto.class);

        Category category = categoryConvertor.fromDto(categoryDto);

        assertEquals(categoryDto.getId(), category.getCategoryId());
        assertEquals(categoryDto.getName(), category.getName());
    }
}
