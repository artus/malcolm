package io.github.artus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeExceptionSupplierTest {

    private RuntimeExceptionSupplier runtimeExceptionSupplier;

    @BeforeEach
    void beforeEach() {
        this.runtimeExceptionSupplier = new RuntimeExceptionSupplier();
    }

    @Test
    void next_returns_a_new_RuntimeException_everytime_with_default_message() {
        final RuntimeException runtimeException1 = runtimeExceptionSupplier.next();
        final RuntimeException runtimeException2 = runtimeExceptionSupplier.next();

        assertNotEquals(runtimeException1, runtimeException2);
        assertEquals(RuntimeExceptionSupplier.MESSAGE, runtimeException1.getMessage());
        assertEquals(RuntimeExceptionSupplier.MESSAGE, runtimeException2.getMessage());
    }

    @Test
    void next_with_String_as_parameter_returns_new_RuntimeException_with_supplied_String_as_message() {
        final String message = "This is a custom error message.";
        RuntimeException runtimeException = runtimeExceptionSupplier.next(message);

        assertEquals(message, runtimeException.getMessage());
    }

    @Test
    void next_with_String_parameter_as_null_returns_new_RuntimeException_with_default_message() {
        String suppliedString = null;
        RuntimeException runtimeException = runtimeExceptionSupplier.next(suppliedString);
        assertEquals(RuntimeExceptionSupplier.MESSAGE, runtimeException.getMessage());
    }

    @Test
    void next_with_Class_parameters_as_null_throws_NullPointerException() {
        Class<? extends Throwable> suppliedClass = null;

        assertThrows(NullPointerException.class, () -> {
            throw runtimeExceptionSupplier.next(suppliedClass);
        });
    }

    @Test
    void next_with_Class_parameter_throws_ThrowableInstantiationException_when_another_Class_than_RuntimeException_is_supplied() {
        Class<? extends Throwable> suppliedClass = IOException.class;

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
            throw runtimeExceptionSupplier.next(RuntimeException.class);
        });

        assertEquals(RuntimeExceptionSupplier.MESSAGE, runtimeException.getMessage());

        assertThrows(ThrowableInstantiationException.class, () -> {
            throw runtimeExceptionSupplier.next(suppliedClass);
        });
    }

    @Test
    void next_with_Class_parameter_can_instantiate_children_of_RuntimeException() {
        final String message = "This is a child of RuntimeException";
        RuntimeException runtimeException = assertDoesNotThrow(() -> runtimeExceptionSupplier.next(ChildOfRuntimeException.class, message));
        assertEquals(message, runtimeException.getMessage());
        assertTrue(runtimeException instanceof ChildOfRuntimeException);
    }

    @Test
    void next_with_Class_parameter_throws_ThrowableInstantiationException_when_supplied_type_is_not_instantiable() {
        ThrowableInstantiationException throwableInstantiationException = assertThrows(ThrowableInstantiationException.class, () -> {
            throw runtimeExceptionSupplier.next(NonInstantiableException.class);
        });
    }

    static final class ChildOfRuntimeException extends RuntimeException {
        public ChildOfRuntimeException(String message) {
            super(message);
        }
    }

    static final class NonInstantiableException extends RuntimeException {
        public NonInstantiableException(String message) throws InstantiationException {
            throw new InstantiationException();
        }
    }
}