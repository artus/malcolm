package io.github.artus.malcolm.suppliers;

public class RuntimeExceptionSupplier implements ThrowableSupplier<RuntimeException> {

    public static final String MESSAGE = "New RuntimeException due to chaotic engineering.";

    @Override
    public RuntimeException supply() {
        return new RuntimeException(MESSAGE);
    }
}
