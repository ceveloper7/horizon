package com.gba.horizon.controller;

import com.gba.horizon.entity.Category;
import com.gba.horizon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/v1/public/categories")
    public Map<Long, Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/api/v1/public/categories")
    public String createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return  "Category added successfully";
    }

    @DeleteMapping("/api/v1/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId){
        String status = categoryService.deleteCategory(categoryId);
        return status;
    }
}
