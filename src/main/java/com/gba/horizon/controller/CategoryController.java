package com.gba.horizon.controller;

import com.gba.horizon.entity.Category;
import com.gba.horizon.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //@GetMapping("/api/v1/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.GET)
    public ResponseEntity<List<Category>> getCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //@PostMapping("/api/v1/public/categories")
    @RequestMapping(value = "/public/categories", method = RequestMethod.POST)
    public ResponseEntity<String> createCategory(@RequestBody Category category){
        categoryService.createCategory(category);
        return  new ResponseEntity<>("Category added successfully", HttpStatus.CREATED  );
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){
        try{
            String status = categoryService.deleteCategory(categoryId);
            // otras formas de usar ResponseEntity
            // return ResponseEntity.ok(status);
            // return ResponseEntity.status(HttpStatus.OK).body(status);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch (ResponseStatusException ex){
            return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Long categoryId){
        try {
            Category cat = categoryService.updateCategory(category, categoryId);
            return new ResponseEntity<>("Category with Category id: " + categoryId + " updated", HttpStatus.OK);
        }catch (ResponseStatusException ex){
            return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        }
    }
}
