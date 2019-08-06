package io.github.artus;

import java.lang.reflect.InvocationTargetException;

public class RuntimeExceptionSupplier implements ThrowableSupplier<RuntimeException> {

    public static final String MESSAGE = "New RuntimeException due to chaotic engineering.";

    @Override
    public RuntimeException next() {
        return this.next(MESSAGE);
    }

    @Override
    public RuntimeException next(String message) {
        return next(RuntimeException.class, message);
    }

    @Override
    public RuntimeException next(Class<? extends Throwable> type) {
        return next(type, MESSAGE);
    }

    @Override
    public RuntimeException next(Class<? extends Throwable> type, String message) {
        if (message == null) message = MESSAGE;

        if (!RuntimeException.class.isAssignableFrom(type)) {
            String errorMessage = String.format("RuntimeExceptionSupplier does not support supplying other types than RuntimeExceptions, '%s' is not a valid type.", type.getSimpleName());
            throw new ThrowableInstantiationException(errorMessage, new InvalidTypeException(errorMessage));
        }

        try {
            return (RuntimeException) type.getConstructor(String.class).newInstance(message);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new ThrowableInstantiationException(e.getMessage(), e);
        }
    }

}
