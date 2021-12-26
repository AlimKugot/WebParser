package com.leti.webparser.repositories;

import com.leti.webparser.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Jpa file that connects to Database and works with product table
 */
@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, String> {

    /**
     * Check if product entity exists with already set parameters
     * @param link to product-page
     * @param price of product
     * @return if product with link and price exists true otherwise false
     */
    boolean existsByLinkAndPrice(String link, Double price);
}
