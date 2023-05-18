package com.zinchenko.admin.category;


import com.zinchenko.admin.category.dto.CategoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryRestControllerV1 {

    private final CategoryService categoryService;

    public CategoryRestControllerV1(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:all')")
    public ResponseEntity<CategoryDto> getById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(categoryService.getCategoryDto(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> create(@RequestBody CategoryDto categoryDto) {
        categoryService.create(categoryDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<Void> update(@RequestBody CategoryDto categoryDto) {
        categoryService.update(categoryDto);
        return ResponseEntity.ok().build();
    }
}
