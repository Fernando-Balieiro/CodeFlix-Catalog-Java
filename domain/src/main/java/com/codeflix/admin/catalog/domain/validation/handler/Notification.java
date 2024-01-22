package com.codeflix.admin.catalog.domain.validation.handler;

import com.codeflix.admin.catalog.domain.exceptions.DomainException;
import com.codeflix.admin.catalog.domain.validation.Error;
import com.codeflix.admin.catalog.domain.validation.Validation;
import com.codeflix.admin.catalog.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> erros;

    private Notification(List<Error> errorList) {
        this.erros = errorList;
    }

    public static Notification notificationFactory(final Throwable t) {
        return notificationFactory(new Error(t.getMessage()));
    }
    public static Notification notificationFactory() {
        return new Notification(new ArrayList<>());
    }

    public static Notification notificationFactory(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(final Error anError) {
        this.erros.add(anError);
        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        this.erros.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final DomainException de) {
            this.erros.addAll(de.getErrors());
        } catch (final Throwable t) {
            this.erros.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.erros;
    }
}
