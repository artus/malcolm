package io.github.artus;

public interface ThrowableSupplier <T extends Throwable> {
    T next() throws ThrowableInstantiationException;
    T next(String message) throws ThrowableInstantiationException;
    T next(Class<? extends Throwable> type)throws ThrowableInstantiationException;
    T next(Class<? extends Throwable> type, String message) throws ThrowableInstantiationException;
}
