package com.gba.horizon.service;

import com.gba.horizon.entity.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService{

    private final Map<Long, Category> categories = new HashMap<Long, Category>();

    @Override
    public List<Category> getAllCategories() {
        return categories.values()
                .stream().toList();
    }

    @Override
    public void createCategory(Category category) {
        categories.put(category.getCategoryId(), category);
    }

    @Override
    public String deleteCategory(Long categoryId){
        Category category =  categories.remove(categoryId);
        if (category != null)
            return "Category with categoryId: " + categoryId + " removed";
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Resource not found"
        );
    }
}
