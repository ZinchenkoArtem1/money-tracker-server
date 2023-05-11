package com.zinchenko.admin.category;

import org.springframework.stereotype.Service;

import java.util.List;

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

    public CategoryDto getById(Integer id) {
        return categoryConvertor.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Category with id [%s] not found".formatted(id)))
        );
    }

    public void create(CategoryDto categoryDto) {
        if (categoryDto.getId() != null) {
            throw new IllegalStateException("Request body must not contain id for the create category operation");
        } else {
            categoryRepository.save(categoryConvertor.fromDto(categoryDto));
        }
    }

    public void update(CategoryDto categoryDto) {
        checkExist(categoryDto.getId());
        categoryRepository.save(categoryConvertor.fromDto(categoryDto));
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