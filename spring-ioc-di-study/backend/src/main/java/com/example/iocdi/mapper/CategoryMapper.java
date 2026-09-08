package com.example.iocdi.mapper;

import com.example.iocdi.domain.Category;
import com.example.iocdi.dto.CategoryRequest;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> findAll();
    Category findById(Long categoryId);
    int insert(CategoryRequest category);
    int delete(Long categoryId);
    int update(Category category);
}
