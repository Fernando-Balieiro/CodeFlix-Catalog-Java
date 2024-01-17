package com.codeflix.admin.catalog.domain.exceptions;

import com.codeflix.admin.catalog.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException {

    private final List<Error> errors;
    private DomainException(final List<Error> errorList) {
        super("", null, true, false);
        this.errors = errorList;
    }

    public static DomainException DomainExceptionFactory(final List<Error> errorList) {
        return new DomainException(errorList);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
