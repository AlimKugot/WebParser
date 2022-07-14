package com.alim.parser.controller;

import com.alim.parser.entity.CategoryEntity;
import com.alim.parser.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoriesController {

    private final CategoryService categoryService;


    @GetMapping
    public List<CategoryEntity> getCategories() {
        return categoryService.getAll();
    }

    @GetMapping(value = "/{id}")
    public List<CategoryEntity> getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }


}
