package com.codeflix.admin.catalog.application.category.update;

import com.codeflix.admin.catalog.application.category.create.CreateCategoryCommand;
import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import com.codeflix.admin.catalog.domain.category.CategoryId;
import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.handler.Notification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanup() {
        Mockito.reset(categoryGateway);
    }

//    1. Teste caminho feliz
//    2. Teste passando propriedades inválidas
//    3. Teste atualizando uma categoria para inválida
//    4. Teste simulando um erro generico vindo do gateway
//    5. Teste atualizar categoria passando ID inválido

    @Test
    public void givenAValidCommand_shouldReturnCategoryId_whenUpdateCategoryIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.categoryFactory(
                "Film",
                null,
                true
        );

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.updateCategoryFactory(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1))
                .findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory ->
                        Objects.equals(expectedName, aUpdateCategory.getName())
                                && Objects.equals(expectedDescription, aUpdateCategory.getDescription())
                                && Objects.equals(expectedIsActive, aUpdateCategory.isActive())
                                && Objects.equals(expectedId, aUpdateCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdateCategory.getUpdatedAt()) // TODO: Fix Assertion
                                && Objects.isNull(aCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_ShouldReturnDomainException_whenUpdateCategoryIsCalled() {
        final var aCategory = Category.categoryFactory("Film", null, true);
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var isActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.updateCategoryFactory(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                isActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.categoryFactory(aCategory)));

        final var notification =
                useCase.execute(aCommand).getLeft();


        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
        Mockito.verify(categoryGateway, times(0)).update(any());

//        TESTE QUEBRAVA POIS ESTAVA FAZENDO CHECAGEM DE VALIDAÇÃO NA CLASSE DE CATEGORIA

    }

    @Test
    public void givenInvalidName_ShouldReturnDomainException_WhenUpdateCategoryIsCalled() {

        final var aCategory =
                Category.categoryFactory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand.updateCategoryFactory(
                        expectedId.getValue(),
                        expectedName,
                        expectedDescription,
                        expectedIsActive
                );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.categoryFactory(aCategory)));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidInactiveCommand_shouldReturnInactiveCategoryId_whenCallsUpdateCategory() {
        final var aCategory = Category.categoryFactory(
                "Film",
                null,
                true
        );


        final var expectedId = aCategory.getId();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var isActive = false;

        final var aCommand = UpdateCategoryCommand.updateCategoryFactory(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                isActive
        );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.categoryFactory(aCategory)));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());


        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput =
                useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito
                .verify(categoryGateway, times(1))
                .findById(eq(expectedId));


        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdatedCategory ->
                        Objects.equals(expectedName, aUpdatedCategory.getName())
                                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                                && Objects.equals(isActive, aUpdatedCategory.isActive())
                                && Objects.equals(expectedId, aUpdatedCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                                && Objects.nonNull(aUpdatedCategory.getDeletedAt())
        ));


    }

    @Test
    public void givenAValidCommand_shouldReturnException_whenGatewayThrowsRandomException() {
        final var aCategory = Category.categoryFactory(
                "Film",
                null,
                true
        );
        final var expectedId = aCategory.getId();
        final var expectedName = "Files";
        final var expectedDescription = "A categoria mais assistida";
        final var isActive = true;
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand.updateCategoryFactory(
                        expectedId.getValue(),
                        expectedName,
                        expectedDescription,
                        isActive
                );

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.categoryFactory(aCategory)));

        when(categoryGateway.update(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                someCategory ->
                        Objects.equals(expectedName, someCategory.getName())
                                && Objects.equals(expectedDescription, someCategory.getDescription())
                                && Objects.equals(isActive, someCategory.isActive())
                                && Objects.nonNull(someCategory.getId())
                                && Objects.nonNull(someCategory.getCreatedAt())
                                && Objects.nonNull(someCategory.getUpdatedAt())
                                && Objects.isNull(someCategory.getDeletedAt())
        ));
    }

    @Test
    public void givenACommandWithInvalidId_shouldReturnNotFound_whenUpdateCategoryIsCalled() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var isActive = false;
        final var expectedId = "1234";

        final var expectedErrorMessage = "Category with ID 1234 was not found";

        final var aCommand = UpdateCategoryCommand.updateCategoryFactory(
                expectedId,
                expectedName,
                expectedDescription,
                isActive
        );

        when(categoryGateway.findById(eq(CategoryId.from(expectedId))))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                DomainException.class, () -> useCase.execute(aCommand)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());


        Mockito.verify(
                        categoryGateway, times(1))
                .findById(eq(CategoryId.from(expectedId)));

        Mockito.verify(categoryGateway, times(0)).update(any());
    }
}
































