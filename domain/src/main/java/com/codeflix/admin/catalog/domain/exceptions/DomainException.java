package com.codeflix.admin.catalog.domain.exceptions;

import com.codeflix.admin.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {

    private final List<Error> errors;
    private DomainException(final String message, final List<Error> errorList) {
        super(message);
        this.errors = errorList;
    }

    public static DomainException DomainExceptionFactory(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException DomainExceptionFactory(final List<Error> errorList) {
        return new DomainException("", errorList);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
