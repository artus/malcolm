package io.github.artus.malcolm.suppliers;

@FunctionalInterface
public interface ThrowableSupplier <T extends Throwable> {
    T supply();
}
