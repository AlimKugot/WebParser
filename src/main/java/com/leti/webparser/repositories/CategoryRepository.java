package com.leti.webparser.repositories;

import com.leti.webparser.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, String> {


    CategoryEntity findFirstBySubCategoryIsNull();

    void deleteDistinctByIdAndSubCategoryIsNull(String id);

    boolean existsByCategoryLink(String link);

    boolean existsBySubCategoryLink(String link);
}
