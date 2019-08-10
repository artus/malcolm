package io.github.artus.malcolm.transformers;

@FunctionalInterface
public interface ThrowableTransformer<T> {
    T transform(Throwable throwable);
}
