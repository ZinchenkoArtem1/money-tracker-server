package com.zinchenko.admin.category.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "category_privatbank")
public class CategoryPrivatbank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_privatbank_id")
    private Integer categoryMccId;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Integer getCategoryMccId() {
        return categoryMccId;
    }

    public CategoryPrivatbank setCategoryMccId(Integer categoryMccId) {
        this.categoryMccId = categoryMccId;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryPrivatbank setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public CategoryPrivatbank setCategory(Category category) {
        this.category = category;
        return this;
    }
}
