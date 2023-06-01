package com.zinchenko.admin.category;

import com.zinchenko.admin.category.domain.*;
import com.zinchenko.admin.category.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    protected static final Category DEFAULT_CATEGORY = new Category().setCategoryId(1);
    private final CategoryConvertor categoryConvertor;
    private final CategoryRepository categoryRepository;
    private final CategoryMonobankRepository categoryMonobankRepository;
    private final CategoryPrivatBankRepository categoryPrivatbankRepository;

    public CategoryService(CategoryConvertor categoryConvertor, CategoryRepository categoryRepository,
                           CategoryMonobankRepository categoryMonobankRepository, CategoryPrivatBankRepository categoryPrivatbankRepository) {
        this.categoryConvertor = categoryConvertor;
        this.categoryRepository = categoryRepository;
        this.categoryMonobankRepository = categoryMonobankRepository;
        this.categoryPrivatbankRepository = categoryPrivatbankRepository;
    }

    public CategoryDto getCategoryDto(Integer id) {
        return categoryConvertor.toDto(getCategoryById(id));
    }

    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Category with id [%s] not found".formatted(id)));
    }

    public List<CategoryDto> getAllCategoriesDto() {
        return categoryRepository.findAll().stream()
                .map(categoryConvertor::toDto)
                .toList();
    }

    public Category getCategoryByMonobankMcc(Integer mcc) {
        return categoryMonobankRepository.findByMcc(mcc)
                .map(CategoryMonobank::getCategory)
                .orElse(DEFAULT_CATEGORY);
    }

    public Category getCategoryByPrivatBankCategoryName(String categoryName) {
        return categoryPrivatbankRepository.findByName(categoryName)
                .map(CategoryPrivatbank::getCategory)
                .orElse(DEFAULT_CATEGORY);
    }
}
