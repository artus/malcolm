package io.github.artus.malcolm.managers;

import io.github.artus.malcolm.exceptions.MissingSupplierException;
import io.github.artus.malcolm.suppliers.ChaoticExceptionSupplier;
import io.github.artus.malcolm.suppliers.ThrowableSupplier;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableSupplierManagerTest {

    @Test
    void NoArgsConstructor_will_set_default_ThrowableSupplier_to_RuntimeExeptionSupplier() {
        assertTrue(new ThrowableSupplierManager().getDefaultThrowableSupplier() instanceof ChaoticExceptionSupplier);
    }

    @Test
    void Instantiating_ThrowableSupplierManager_using_map_will_set_defaultThrowableSupplier_to_null() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");

        Map<Method, ThrowableSupplier> throwableSuppliers = new HashMap<>();
        throwableSuppliers.put(getMethod, new ChaoticExceptionSupplier());
        ThrowableSupplierManager throwableSupplierManager = new ThrowableSupplierManager(throwableSuppliers);

        assertNull(throwableSupplierManager.getDefaultThrowableSupplier());
        assertEquals(throwableSuppliers, throwableSupplierManager.getThrowableSuppliers());
    }

    @Test
    void interventionIsRequired_returns_false_when_defaultThrowableSupplier_is_null_and_no_ThrowableSupplier_was_found_for_method() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        Method someStringMethod = InnerClass.class.getMethod("someString");

        Map<Method, ThrowableSupplier> throwableSuppliers = new HashMap<>();
        throwableSuppliers.put(getMethod, new ChaoticExceptionSupplier());
        ThrowableSupplierManager throwableSupplierManager = new ThrowableSupplierManager(throwableSuppliers);

        assertTrue(throwableSupplierManager.interventionIsRequired(getMethod));
        assertFalse(throwableSupplierManager.interventionIsRequired(someStringMethod));
    }

    @Test
    void supply_throws_MissingSupplierException_when_method_is_passed_that_has_no_specialized_Supplier_and_default_Supplier_is_null() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        Method someStringMethod = InnerClass.class.getMethod("someString");

        Map<Method, ThrowableSupplier> throwableSuppliers = new HashMap<>();
        throwableSuppliers.put(getMethod, new ChaoticExceptionSupplier());
        ThrowableSupplierManager throwableSupplierManager = new ThrowableSupplierManager(throwableSuppliers);

        MissingSupplierException missingSupplierException = assertThrows(MissingSupplierException.class, () -> {
            throw throwableSupplierManager.supply(someStringMethod);
        });
        assertTrue(missingSupplierException.getMessage().contains(someStringMethod.getName()));
    }

    @Test
    void supply_uses_default_ThrowableSupplier_when_no_specialized_Supplier_was_found_for_method() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        Method someStringMethod = InnerClass.class.getMethod("someString");

        String message = "This is the errormessage for the default ThrowableSupplier.";

        Map<Method, ThrowableSupplier> throwableSuppliers = new HashMap<>();
        throwableSuppliers.put(getMethod, new ChaoticExceptionSupplier());
        ThrowableSupplierManager throwableSupplierManager = new ThrowableSupplierManager(() -> new ClassNotFoundException(message), throwableSuppliers);

        Throwable throwable = throwableSupplierManager.supply(someStringMethod);
        assertTrue(throwable instanceof ClassNotFoundException);
        assertEquals(message, throwable.getMessage());
    }

    @Test
    void supply_uses_specialized_ThrowableSupplier_when_one_is_found_for_method() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        Method someStringMethod = InnerClass.class.getMethod("someString");

        String message = "This is the errormessage for the default ThrowableSupplier.";

        Map<Method, ThrowableSupplier> throwableSuppliers = new HashMap<>();
        throwableSuppliers.put(getMethod, new ChaoticExceptionSupplier());
        ThrowableSupplierManager throwableSupplierManager = new ThrowableSupplierManager(() -> new ClassNotFoundException(message), throwableSuppliers);

        Throwable throwable = throwableSupplierManager.supply(getMethod);
        assertTrue(throwable instanceof RuntimeException);
        assertEquals(ChaoticExceptionSupplier.MESSAGE, throwable.getMessage());
    }

    private static class InnerClass {
        public boolean get() {
            return true;
        }

        public String someString() {
            return "some string";
        }
    }
}