package io.github.artus;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableInstantiationExceptionTest {

    @Test
    void instantiating_ThrowableInstantationExceptionTest_with_String_parameter_will_cal_super_constructor_with_String_parameter() {
        String message = "test";
        ThrowableInstantiationException throwableInstantiationException = new ThrowableInstantiationException(message);
        assertEquals(message, throwableInstantiationException.getMessage());
    }

    @Test
    void instantiating_ThrowableInstantiationException_with_String_parameter_and_Throwable_parameter_will_add_Throwable_as_cause() {
        String message = "test";
        RuntimeException runtimeException = new RuntimeException();
        ThrowableInstantiationException throwableInstantiationException = new ThrowableInstantiationException(message, runtimeException);
        assertEquals(runtimeException, throwableInstantiationException.getCause());
    }

}