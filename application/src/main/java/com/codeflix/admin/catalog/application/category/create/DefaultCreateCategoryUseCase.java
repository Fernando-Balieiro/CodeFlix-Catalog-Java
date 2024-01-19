package com.codeflix.admin.catalog.application.category.create;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase implements CreateCategoryUseCase {

    private final CategoryGateway gateway;

    public DefaultCreateCategoryUseCase(CategoryGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand anInput) {

        final var aCategory = Category.categoryFactory(
                anInput.name(),
                anInput.description(),
                anInput.isActive()
        );

        aCategory.validate(new ThrowsValidationHandler());

        return CreateCategoryOutput.from(this.gateway.create(aCategory));

    }
}
