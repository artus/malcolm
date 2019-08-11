package io.github.artus.malcolm.suppliers;

import io.github.artus.malcolm.exceptions.ChaoticException;

public class ChaoticExceptionSupplier implements ThrowableSupplier<ChaoticException> {

    public static final String MESSAGE = "New ChaoticException due to chaotic engineering.";

    @Override
    public ChaoticException supply() {
        return new ChaoticException(MESSAGE);
    }
}
