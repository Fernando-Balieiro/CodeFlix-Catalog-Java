package com.codeflix.admin.catalog.application;

public interface UseCase<INPUT, OUTPUT> {
    OUTPUT execute(INPUT anInput);

}
