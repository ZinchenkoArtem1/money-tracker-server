package com.zinchenko.admin.category;

import com.zinchenko.RandomGenerator;
import com.zinchenko.admin.category.domain.*;
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

import static com.zinchenko.admin.category.CategoryService.DEFAULT_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest extends RandomGenerator {

    private CategoryService categoryService;

    @Mock
    private CategoryMonobankRepository categoryMonobankRepository;
    @Mock
    private CategoryPrivatBankRepository categoryPrivatBankRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryConvertor categoryConvertor;

    @BeforeEach
    void setUp() {
        this.categoryService = new CategoryService(categoryConvertor, categoryRepository,
                categoryMonobankRepository, categoryPrivatBankRepository);
    }

    @Test
    void findAllNotEmptyTest() {
        Category category1 = random(Category.class);
        Category category2 = random(Category.class);
        CategoryDto categoryDto1 = random(CategoryDto.class);
        CategoryDto categoryDto2 = random(CategoryDto.class);

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));
        when(categoryConvertor.toDto(category1)).thenReturn(categoryDto1);
        when(categoryConvertor.toDto(category2)).thenReturn(categoryDto2);

        List<CategoryDto> categoriesDto = categoryService.getAllCategoriesDto();

        Assertions.assertEquals(2, categoriesDto.size());
        Assertions.assertEquals(categoryDto1, categoriesDto.get(0));
        Assertions.assertEquals(categoryDto2, categoriesDto.get(1));
    }

    @Test
    void findAllEmptyTest() {
        when(categoryRepository.findAll()).thenReturn(List.of());

        Assertions.assertTrue(categoryService.getAllCategoriesDto().isEmpty());
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
    void getCategoryByIdSuccessTest() {
        Category category = random(Category.class);

        when(categoryRepository.findById(category.getCategoryId())).thenReturn(Optional.of(category));

        assertEquals(category, categoryService.getCategoryById(category.getCategoryId()));
    }

    @Test
    void getCategoryByIdFailedTest() {
        Integer id = RandomUtils.nextInt();

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        IllegalStateException exc = assertThrows(IllegalStateException.class, () -> categoryService.getCategoryById(id));
        assertEquals("Category with id [%s] not found".formatted(id), exc.getMessage());
    }

    @Test
    void getCategoryByMonobankMccTest() {
        CategoryMonobank categoryMonobank = random(CategoryMonobank.class);

        when(categoryMonobankRepository.findByMcc(categoryMonobank.getMcc())).thenReturn(Optional.of(categoryMonobank));

        assertEquals(categoryMonobank.getCategory(), categoryService.getCategoryByMonobankMcc(categoryMonobank.getMcc()));
    }

    @Test
    void getCategoryByMonobankMccUnknownTest() {
        CategoryMonobank categoryMonobank = random(CategoryMonobank.class);

        when(categoryMonobankRepository.findByMcc(categoryMonobank.getMcc())).thenReturn(Optional.empty());

        assertEquals(DEFAULT_CATEGORY, categoryService.getCategoryByMonobankMcc(categoryMonobank.getMcc()));
    }

    @Test
    void getCategoryByPrivatBankCategoryNameTest() {
        CategoryPrivatbank categoryPrivatbank = random(CategoryPrivatbank.class);

        when(categoryPrivatBankRepository.findByName(categoryPrivatbank.getName()))
                .thenReturn(Optional.of(categoryPrivatbank));

        assertEquals(categoryPrivatbank.getCategory(),
                categoryService.getCategoryByPrivatBankCategoryName(categoryPrivatbank.getName())
        );
    }

    @Test
    void getCategoryByPrivatBankCategoryNameUnknownTest() {
        CategoryPrivatbank categoryPrivatbank = random(CategoryPrivatbank.class);

        when(categoryPrivatBankRepository.findByName(categoryPrivatbank.getName())).thenReturn(Optional.empty());

        assertEquals(DEFAULT_CATEGORY, categoryService.getCategoryByPrivatBankCategoryName(categoryPrivatbank.getName()));
    }
}
