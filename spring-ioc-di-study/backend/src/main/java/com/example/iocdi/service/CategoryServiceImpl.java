package com.example.iocdi.service;

import com.example.iocdi.domain.Category;
import com.example.iocdi.dto.CategoryRequest;
import com.example.iocdi.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<Category> getCategories() {

        return categoryMapper.findAll();
    }

    @Override
    public Category getCategory(Long categoryId) {

        return categoryMapper.findById(categoryId);
    }

    @Override
    public void createCategory(CategoryRequest request) {

        categoryMapper.insert(request);
    }

    @Override
    public void deleteCategory(Long categoryId) {

        categoryMapper.delete(categoryId);
    }
    @Override
    public void updateCategory(Long categoryId, CategoryRequest request) {

        categoryMapper.update(categoryId);
    }
}