package com.gba.horizon.service;

import com.gba.horizon.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<Long,Category> getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long categoryId);
}
