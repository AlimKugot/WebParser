package com.leti.webparser.repositories;

import com.leti.webparser.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Jpa file that connects to Database and works with category table
 */
@Repository
public interface CategoryRepository extends CrudRepository<CategoryEntity, String> {



    boolean existsByCategoryLink(String link);

    boolean existsBySubCategoryLink(String link);
}
