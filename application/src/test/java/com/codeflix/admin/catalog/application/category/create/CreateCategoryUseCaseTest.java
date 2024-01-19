package com.codeflix.admin.catalog.application.category.create;

import org.junit.jupiter.api.Test;

public class CreateCategoryUseCaseTest {

    @Test
    public void givenValidParams_ShouldReturnCategoryId_WhenCreateCategoryIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
    }
}
