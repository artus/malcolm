package io.github.artus.transformers;

@FunctionalInterface
public interface ThrowableTransformer<T> {
    T transform(Throwable throwable);
}
