package com.leti.webparser.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "category")
public class CategoryEntity extends CreatedEntity {

    @Column(name = "shop_name", nullable = false)
    private String shopName;


    @Column(name = "main_category")
    private String mainCategory;

    @Column(name = "category")
    private String category;

    @Column(name = "sub_category")
    private String subCategory;


    @Column(name = "main_category_link")
    private String mainCategoryLink;

    @Column(name = "category_link")
    private String categoryLink;

    @Column(name = "sub_category_link")
    private String subCategoryLink;
}
