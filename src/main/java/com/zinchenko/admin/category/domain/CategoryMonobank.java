package com.zinchenko.admin.category.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "category_monobank")
public class CategoryMonobank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_monobank_id")
    private Integer categoryMccId;

    @Column(name = "mcc")
    private Integer mcc;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Integer getCategoryMccId() {
        return categoryMccId;
    }

    public CategoryMonobank setCategoryMccId(Integer categoryMccId) {
        this.categoryMccId = categoryMccId;
        return this;
    }

    public Integer getMcc() {
        return mcc;
    }

    public CategoryMonobank setMcc(Integer mcc) {
        this.mcc = mcc;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public CategoryMonobank setCategory(Category category) {
        this.category = category;
        return this;
    }
}
