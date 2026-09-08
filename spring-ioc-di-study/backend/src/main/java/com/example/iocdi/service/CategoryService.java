package com.example.iocdi.service;

import com.example.iocdi.domain.Category;
import com.example.iocdi.dto.CategoryRequest;

import java.util.List;

public interface CategoryService {

    List<Category> getCategories();

    Category getCategory(Long categoryId);

    void createCategory(CategoryRequest request);

    void deleteCategory(Long categoryId);
    void updateCategory(Long categoryId, CategoryRequest request);
}