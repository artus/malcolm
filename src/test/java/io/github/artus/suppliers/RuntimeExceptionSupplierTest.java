package io.github.artus.suppliers;

import io.github.artus.suppliers.RuntimeExceptionSupplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RuntimeExceptionSupplierTest {

    private RuntimeExceptionSupplier runtimeExceptionSupplier;

    @BeforeEach
    void beforeEach() {
        this.runtimeExceptionSupplier = new RuntimeExceptionSupplier();
    }

    @Test
    void next_returns_new_RuntimeException_with_default_String_as_message() {
        RuntimeException runtimeException = runtimeExceptionSupplier.supply();

        assertEquals(RuntimeExceptionSupplier.MESSAGE, runtimeException.getMessage());
    }
}