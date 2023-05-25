package com.zinchenko.admin.category;

import com.zinchenko.admin.category.domain.Category;
import com.zinchenko.admin.category.domain.CategoryRepository;
import com.zinchenko.admin.category.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryConvertor categoryConvertor;
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryConvertor categoryConvertor, CategoryRepository categoryRepository) {
        this.categoryConvertor = categoryConvertor;
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(categoryConvertor::toDto)
                .toList();
    }

    public CategoryDto getCategoryDto(Integer id) {
        return categoryConvertor.toDto(getCategory(id));
    }

    public Category getCategory(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Category with id [%s] not found".formatted(id)));
    }

    public Optional<Category> findCategoryByMcc(Integer mcc) {
        return categoryRepository.findByMcc(mcc);
    }

    public void create(CategoryDto categoryDto) {
        if (categoryDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create category operation");
        } else {
            categoryRepository.save(categoryConvertor.fromDto(categoryDto));
        }
    }

    public void update(CategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getId());
        category.setName(categoryDto.getName());

        categoryRepository.save(category);
    }

    public void deleteById(Integer id) {
        checkExist(id);
        categoryRepository.deleteById(id);
    }

    private void checkExist(Integer id) {
        if (!categoryRepository.existsById(id)) {
            throw new IllegalStateException("Category with id [%s] not found".formatted(id));
        }
    }
}
