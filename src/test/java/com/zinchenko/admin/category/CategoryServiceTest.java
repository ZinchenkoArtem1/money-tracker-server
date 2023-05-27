package com.zinchenko.admin.category;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.category.domain.CategoryMccRepository;
import com.zinchenko.admin.category.domain.CategoryRepository;
import com.zinchenko.admin.category.dto.CategoryDto;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest extends RandomGenerator {

    private CategoryService categoryService;

    @Mock
    private CategoryMccRepository categoryMccRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryConvertor categoryConvertor;

    @BeforeEach
    void setUp() {
        this.categoryService = new CategoryService(categoryConvertor, categoryRepository, categoryMccRepository);
    }

    @Test
    void findAllNotEmptyTest() {
        Category category1 = random(Category.class);
        Category category2 = random(Category.class);

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<CategoryDto> categoriesDto = categoryService.findAll();

        Assertions.assertEquals(2, categoriesDto.size());
    }

    @Test
    void findAllEmptyTest() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        Assertions.assertTrue(categoryService.findAll().isEmpty());
    }

    @Test
    void getCategoryDtoSuccessTest() {
        Category category = random(Category.class);
        CategoryDto categoryDto = random(CategoryDto.class);

        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));
        when(categoryConvertor.toDto(category)).thenReturn(categoryDto);

        assertEquals(categoryDto, categoryService.getCategoryDto(category.getCategoryId()));
    }

    @Test
    void getCategoryDtoFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> categoryService.getCategoryDto(id));
        assertEquals("Category with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void createSuccessTest() {
        CategoryDto categoryDto = random(CategoryDto.class)
                .setId(null);
        Category category = random(Category.class);

        when(categoryConvertor.fromDto(categoryDto)).thenReturn(category);
        categoryService.create(categoryDto);

        verify(categoryRepository).save(category);
    }

    @Test
    void createWithIdInRequestTest() {
        CategoryDto categoryDto = mock(CategoryDto.class);

        when(categoryDto.getId()).thenReturn(RandomUtils.nextInt());
        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> categoryService.create(categoryDto));

        assertEquals("Request body must not contain id for the create category operation", exc.getMessage());
        verifyNoInteractions(categoryConvertor);
    }

    @Test
    void updateExistTest() {
        Integer id = RandomUtils.nextInt();
        String newName = UUID.randomUUID().toString();
        CategoryDto categoryDto = mock(CategoryDto.class);
        Category category = mock(Category.class);

        when(categoryDto.getId()).thenReturn(id);
        when(categoryDto.getName()).thenReturn(newName);
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));

        categoryService.update(categoryDto);

        verify(category).setName(newName);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateNotExistTest() {
        Integer id = RandomUtils.nextInt();
        CategoryDto categoryDto = mock(CategoryDto.class);

        when(categoryDto.getId()).thenReturn(id);
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> categoryService.update(categoryDto));
        assertEquals("Category with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void deleteByIdNotExistTest() {
        Integer id = RandomUtils.nextInt();

        when(categoryRepository.existsById(id)).thenReturn(false);

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> categoryService.deleteById(id));
        assertEquals("Category with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void deleteByIdSuccessTest() {
        Integer id = RandomUtils.nextInt();

        when(categoryRepository.existsById(id)).thenReturn(true);

        categoryService.deleteById(id);

        verify(categoryRepository).deleteById(id);
    }
}
