package com.alim.parser.service;

import com.alim.parser.entity.CategoryEntity;

import java.util.List;

public interface CategoryService {

    List<CategoryEntity> getAll();

    List<CategoryEntity> getById(Long id);
}
