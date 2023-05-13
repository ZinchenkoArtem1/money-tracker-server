package com.zinchenko.admin.category;


import com.zinchenko.admin.category.dto.CategoryDto;
import com.zinchenko.common.error.BasicErrorResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin/categories")
public class CategoryRestControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(CategoryRestControllerV1.class);
    private final CategoryService categoryService;

    public CategoryRestControllerV1(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<CategoryDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BasicErrorResponse> handleException(Exception ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.internalServerError().body(
                new BasicErrorResponse("Internal server error")
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BasicErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ExceptionUtils.getMessage(ex), ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new BasicErrorResponse("Access Denied")
        );
    }
}
