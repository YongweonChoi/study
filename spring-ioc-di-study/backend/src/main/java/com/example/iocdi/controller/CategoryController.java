package com.example.iocdi.controller;

import com.example.iocdi.domain.Category;
import com.example.iocdi.dto.CategoryRequest;
import com.example.iocdi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getCategories() {

        return categoryService.getCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategory(
            @PathVariable Long categoryId
    ) {

        return categoryService.getCategory(categoryId);
    }

    @PostMapping
    public void createCategory(
            @RequestBody CategoryRequest request
    ) {

        categoryService.createCategory(request);
    }

    @DeleteMapping("/{categoryId}")
    public void deleteCategory(
            @PathVariable Long categoryId
    ) {

        categoryService.deleteCategory(categoryId);
    }

    @PutMapping("/{categoryId}")
    public void updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest request
    ) {
        categoryService.updateCategory(categoryId, request);
    }
}