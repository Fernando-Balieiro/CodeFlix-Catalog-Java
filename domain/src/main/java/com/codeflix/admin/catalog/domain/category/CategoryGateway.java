package com.codeflix.admin.catalog.domain.category;

import com.codeflix.admin.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);
    void deleteById(CategoryId categoryId);
    Optional<Category> findById(CategoryId categoryId);
    Category update(Category category);
    Pagination<Category> findAll(CategorySearchQuery categorySearchQuery);
}
