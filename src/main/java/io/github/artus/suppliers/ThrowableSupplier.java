package io.github.artus.suppliers;

@FunctionalInterface
public interface ThrowableSupplier <T extends Throwable> {
    T supply();
}
