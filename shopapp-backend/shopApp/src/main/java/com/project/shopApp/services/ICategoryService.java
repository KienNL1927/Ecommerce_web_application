package com.project.shopApp.services;

import com.project.shopApp.dtos.CategoryDTO;
import com.project.shopApp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);

    List<Category> getAllCategories();

    Category updateCategory(long categoryId, CategoryDTO categoryDTO);

    void deleteCategory(long id);
}
