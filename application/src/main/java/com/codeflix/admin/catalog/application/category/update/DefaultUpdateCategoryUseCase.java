package com.codeflix.admin.catalog.application.category.update;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryId;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand anInput) {
        final var anId = CategoryId.from(anInput.id());
        final String name = anInput.name();
        final String description = anInput.description();
        final boolean isActive = anInput.isActive();

        final var aCategory = this.categoryGateway.findById(anId).orElseThrow(notFound(anId));

        final var notification = Notification.notificationFactory();
        aCategory
                .update(name, description, isActive)
                .validate(notification);

        return notification.hasErrors() ? API.Left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(
                        Notification::notificationFactory,
                        UpdateCategoryOutput::updateCategoryOutputFactory
                );
    }

    private Supplier<DomainException> notFound(CategoryId anId) {
        return () -> DomainException.DomainExceptionFactory(
                new Error(
                        "Category with ID %s was not found".formatted(anId.getValue())
                )
        );
    }
}
