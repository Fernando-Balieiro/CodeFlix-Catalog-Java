package com.codeflix.admin.catalog.application.category.create;

import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;

public class CreateCategoryUseCaseTest {

    @Test
    public void givenValidCommand_ShouldReturnCategoryId_WhenCreateCategoryIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.createCategoryCommandFactory(
                expectedName, expectedDescription, expectedIsActive
        );

        final var categoryGateway = Mockito.mock(CategoryGateway.class);
        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());


        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        final var actualOutput = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Mockito.verify(categoryGateway, times(1))
                .create(argThat(aCategory ->
                                Objects.equals(
                                        expectedName, aCategory.getName())
                                        && Objects.equals(expectedDescription, aCategory.getDescription())
                                        && Objects.equals(expectedIsActive, aCategory.isActive())
                                        && Objects.nonNull(aCategory.getId())
                                        && Objects.nonNull(aCategory.getCreatedAt())
                                        && Objects.nonNull(aCategory.getUpdatedAt())
                                        && Objects.isNull(aCategory.getDeletedAt()
                                )
                        )
                );
    }
}
