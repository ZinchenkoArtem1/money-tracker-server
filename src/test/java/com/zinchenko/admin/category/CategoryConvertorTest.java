package com.zinchenko.admin.category;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.category.dto.CategoryDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CategoryConvertorTest {

    private CategoryConvertor categoryConvertor;

    @BeforeEach
    void setUp() {
        categoryConvertor = new CategoryConvertor();
    }

    @Test
    void toDtoTest() {
        Category category = new Category()
                .setCategoryId(RandomUtils.nextInt())
                .setName(UUID.randomUUID().toString());

        CategoryDto categoryDto = categoryConvertor.toDto(category);

        assertEquals(category.getCategoryId(), categoryDto.getId());
        assertEquals(category.getName(), categoryDto.getName());
    }

    @Test
    void fromDtoTest() {
        CategoryDto categoryDto = new CategoryDto()
                .setId(RandomUtils.nextInt())
                .setName(UUID.randomUUID().toString());

        Category category = categoryConvertor.fromDto(categoryDto);

        assertEquals(categoryDto.getId(), category.getCategoryId());
        assertEquals(categoryDto.getName(), category.getName());
    }
}
