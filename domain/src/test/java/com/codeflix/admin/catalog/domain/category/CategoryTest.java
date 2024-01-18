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

    @Test
    public void givenInvalidEmptyName_ThrowDomainException() {
        final String name = " ";
        final var description = "a categoria mais assistida";
        final var isActive = true;

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var createdCategory =
                Category.categoryFactory(name, description, isActive);

        final var exception = Assertions.assertThrows(
                DomainException.class, () -> createdCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }
    @Test
    public void givenInvalidNameLengthLessThan3_ThrowDomainException() {
        final String name = "fi";
        final var description = "a categoria mais assistida";
        final var isActive = true;

        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var createdCategory =
                Category.categoryFactory(name, description, isActive);

        final var exception = Assertions.assertThrows(
                DomainException.class, () -> createdCategory.validate(new ThrowsValidationHandler())
        );

        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameLengthMoreThan255_ThrowsDomainException() {
        final String name = "teste".repeat(256);
        final var description = "a categoria mais assistida";
        final var isActive = true;

        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var createdCategory =
                Category.categoryFactory(name, description, isActive);

        final var exception = Assertions.assertThrows(
                DomainException.class, () -> createdCategory.validate(new ThrowsValidationHandler())
        );
        Assertions.assertEquals(expectedErrorCount, exception.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, exception.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_ShouldBeOk() {
        final var name = "Filme";
        final var description = " ";
        final var isActive = true;
        final var createdCategory =
                Category.categoryFactory(name, description, isActive);

        Assertions.assertDoesNotThrow(() -> createdCategory.validate(
                new ThrowsValidationHandler())
        );
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
    public void givenAValidFalseIsActive_ShouldBeOk() {
        final var name = "Filme";
        final var description = "a categoria mais assistida";
        final var isActive = false;

        final var createdCategory =
                Category.categoryFactory(name, description, isActive);

        Assertions.assertDoesNotThrow(() -> createdCategory.validate(
                new ThrowsValidationHandler())
        );
        Assertions.assertNotNull(createdCategory);
        Assertions.assertNotNull(createdCategory.getId());
        Assertions.assertEquals(name, createdCategory.getName());
        Assertions.assertEquals(description, createdCategory.getDescription());
        Assertions.assertFalse(createdCategory.isActive());
        Assertions.assertNotNull(createdCategory.getCreatedAt());
        Assertions.assertNotNull(createdCategory.getUpdatedAt());
        Assertions.assertNotNull(createdCategory.getDeletedAt());
    }

    @Test
    public void givenValidActiveCategory_DeactivateAccountWhenDeactivateIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var createdCategory =
                Category.categoryFactory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(() -> createdCategory.validate(
                new ThrowsValidationHandler())
        );

        final var updatedAt = createdCategory.getUpdatedAt();


        Assertions.assertTrue(createdCategory.isActive());
        Assertions.assertNull(createdCategory.getDeletedAt());

        final var actualCategory = createdCategory.deactivate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(
                new ThrowsValidationHandler())
        );

        Assertions.assertEquals(actualCategory.getId(), createdCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertFalse(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_ActivateAccountWhenActivateIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var createdCategory =
                Category.categoryFactory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(() -> createdCategory.validate(
                new ThrowsValidationHandler())
        );

        final var createdAt = createdCategory.getCreatedAt();
        final var updatedAt = createdCategory.getUpdatedAt();

        Assertions.assertFalse(createdCategory.isActive());
        Assertions.assertNotNull(createdCategory.getDeletedAt());

        final var actualCategory = createdCategory.activate();

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(
                new ThrowsValidationHandler())
        );

        Assertions.assertEquals(actualCategory.getId(), createdCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_ReturnUpdatedCategoryWhenUpdateIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var createdCategory =
                Category.categoryFactory("fil", "A categoria ma", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> createdCategory.validate(
                new ThrowsValidationHandler())
        );

        final var createdAt = createdCategory.getCreatedAt();
        final var updatedAt = createdCategory.getUpdatedAt();

        final var updatedCategory = createdCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> updatedCategory.validate(
                new ThrowsValidationHandler())
        );

        Assertions.assertEquals(createdCategory.getId(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeletedAt());
    }


    @Test
    public void givenValidCategory_UpdateToInactive() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var createdCategory =
                Category.categoryFactory("fil", "A categoria ma", true);

        Assertions.assertDoesNotThrow(() -> createdCategory.validate(
                new ThrowsValidationHandler())
        );

        Assertions.assertTrue(createdCategory.isActive());
        Assertions.assertNull(createdCategory.getDeletedAt());

        final var createdAt = createdCategory.getCreatedAt();
        final var updatedAt = createdCategory.getUpdatedAt();

        final var updatedCategory = createdCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> updatedCategory.validate(
                new ThrowsValidationHandler())
        );

        Assertions.assertFalse(updatedCategory.isActive());
        Assertions.assertNotNull(updatedCategory.getDeletedAt());

        Assertions.assertEquals(createdCategory.getId(), updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
    }

    @Test
    public void givenAValidCategory_ShouldThrowWhenGivenInvalidUpdateParams() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var createdCategory = Category.categoryFactory(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertThrows(DomainException.class,
                () -> createdCategory.update("fi", expectedDescription, expectedIsActive)
        );
    }
}

