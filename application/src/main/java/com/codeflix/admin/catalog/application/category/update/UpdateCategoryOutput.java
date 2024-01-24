package com.codeflix.admin.catalog.application.category.update;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryId;

public record UpdateCategoryOutput(
        CategoryId id
) {

    public static UpdateCategoryOutput updateCategoryOutputFactory(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
