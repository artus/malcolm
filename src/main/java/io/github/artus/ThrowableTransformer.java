package io.github.artus;

@FunctionalInterface
public interface ThrowableTransformer<T> {
    T transform(Throwable throwable);
}
