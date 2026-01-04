package com.gba.horizon.service;

import com.gba.horizon.entity.Category;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

    private final Map<Long, Category> categories = new HashMap<Long, Category>();

    @Override
    public Map<Long, Category> getAllCategories() {
        return Collections.unmodifiableMap(categories);
    }

    @Override
    public void createCategory(Category category) {
        categories.put(category.getCategoryId(), category);
    }

    @Override
    public String deleteCategory(Long categoryId){
        categories.remove(categoryId);
        return "Category with categoryId: " + categoryId + " removed";
    }
}
