package com.gba.horizon.entity;

public class Category {
    private static Long idx = 1L;
    private Long categoryId;
    private String categoryName;

    {
        this.categoryId = idx;
        idx++;
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
