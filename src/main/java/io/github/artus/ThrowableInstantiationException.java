package io.github.artus;

public class ThrowableInstantiationException extends RuntimeException {
    public ThrowableInstantiationException(String message) {
        super(message);
    }

    public ThrowableInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
