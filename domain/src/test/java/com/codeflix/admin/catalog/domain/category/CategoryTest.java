package com.codeflix.admin.catalog.domain.category;

import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    public void givenValidParams_InstantiateCategory() {
            final var name = "Filme";
            final var description = "a categoria mais assistida";
            final var isActive = true;

            final var createdCategory =
                    Category.categoryFactory(name, description, isActive);

        Assertions.assertNotNull(createdCategory);
        Assertions.assertNotNull(createdCategory.getId());
        Assertions.assertEquals(name, createdCategory.getName());
        Assertions.assertEquals(description, createdCategory.getDescription());
        Assertions.assertEquals(isActive, createdCategory.isActive());
        Assertions.assertNotNull(createdCategory.getCreatedAt());
        Assertions.assertNotNull(createdCategory.getUpdatedAt());
        Assertions.assertNull(createdCategory.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_ShouldRecieveError() {
        final String name = null;
        final var description = "a categoria mais assistida";
        final var isActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var createdCategory =
                Category.categoryFactory(name, description, isActive);

        final var exception = Assertions.assertThrows(
                DomainException.class, () -> createdCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
}