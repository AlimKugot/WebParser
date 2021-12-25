package com.leti.webparser.repositories;

import com.leti.webparser.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, String> {

    boolean existsByLinkAndPrice(String link, Double price);
}
