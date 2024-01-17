package com.codeflix.admin.catalog.domain.validation.handler;

import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.Validation;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.DomainExceptionFactory(List.of(anError));
    }

    @Override
    public ValidationHandler append(final ValidationHandler aHandler) {
//        final var error = aHandler.getErrors().getFirst();
        throw DomainException.DomainExceptionFactory(aHandler.getErrors());
    }

    @Override
    public ValidationHandler validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (Exception ex) {
            throw DomainException.DomainExceptionFactory(
                    List.of(new Error(ex.getMessage()))
            );
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
