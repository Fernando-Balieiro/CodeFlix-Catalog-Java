package com.codeflix.admin.catalog.application.category.update;

import com.codeflix.admin.catalog.domain.category.Category;
import com.codeflix.admin.catalog.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {
    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;
    @Mock
    private CategoryGateway categoryGateway;

//    1. Teste caminho feliz
//    2. Teste passando propriedades inválidas
//    3. Teste atualizando uma categoria para inválida
//    4. Teste simulando um erro generico vindo do gateway
//    5. Teste atualizar categoria passando ID inválido

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
        final var aCommand = UpdateCategoryCommand.UpdateCategoryFactory(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.of(aCategory));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, times(1))
                .findById(eq(expectedId));

        Mockito.verify(categoryGateway, times(1)).update(argThat(
                aUpdateCategory ->
                        Objects.equals(expectedName, aCategory.getName())
                                && Objects.equals(expectedDescription, aCategory.getDescription())
                                && Objects.equals(expectedIsActive, aCategory.isActive())
                                && Objects.equals(expectedId, aCategory.getId())
                                && Objects.equals(aCategory.getCreatedAt(), aUpdateCategory.getCreatedAt())
                                && aCategory.getCreatedAt().isBefore(aUpdateCategory.getUpdatedAt())
                                && Objects.isNull(aCategory.getDeletedAt())
        ));
    }
}
































