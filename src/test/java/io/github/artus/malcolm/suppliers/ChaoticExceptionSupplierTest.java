package io.github.artus.malcolm.suppliers;

import io.github.artus.malcolm.exceptions.ChaoticException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChaoticExceptionSupplierTest {

    private ChaoticExceptionSupplier chaoticExceptionSupplier;

    @BeforeEach
    void beforeEach() {
        this.chaoticExceptionSupplier = new ChaoticExceptionSupplier();
    }

    @Test
    void next_returns_new_RuntimeException_with_default_String_as_message() {
        ChaoticException chaoticException = chaoticExceptionSupplier.supply();

        assertEquals(ChaoticExceptionSupplier.MESSAGE, chaoticException.getMessage());
    }
}