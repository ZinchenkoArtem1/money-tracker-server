package com.zinchenko.admin.category.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "category_mcc")
public class CategoryMcc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_mcc_id")
    private Integer categoryMccId;

    @Column(name = "mcc")
    private Integer mcc;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Integer getCategoryMccId() {
        return categoryMccId;
    }

    public CategoryMcc setCategoryMccId(Integer categoryMccId) {
        this.categoryMccId = categoryMccId;
        return this;
    }

    public Integer getMcc() {
        return mcc;
    }

    public CategoryMcc setMcc(Integer mcc) {
        this.mcc = mcc;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public CategoryMcc setCategory(Category category) {
        this.category = category;
        return this;
    }
}
