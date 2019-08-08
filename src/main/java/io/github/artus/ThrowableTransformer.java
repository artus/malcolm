package io.github.artus;

public interface ThrowableTransformer<T> {
    T transform(Throwable throwable);
}
