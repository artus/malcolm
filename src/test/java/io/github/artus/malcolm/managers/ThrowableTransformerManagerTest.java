package io.github.artus.malcolm.managers;

import io.github.artus.malcolm.exceptions.MissingTransformerException;
import io.github.artus.malcolm.transformers.ThrowableTransformer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ThrowableTransformerManagerTest {

    @Test
    void instantiating_ThrowableTransformerManager_with_only_default_transformer_always_returns_true_on_transformationIsRequired() throws NoSuchMethodException {
        ThrowableTransformerManager throwableTransformerManager = new ThrowableTransformerManager(throwable -> true);
        Method getMethod = InnerClass.class.getMethod("get");
        assertTrue(throwableTransformerManager.transformationIsRequired(getMethod));
    }

    @Test
    void instantiating_ThrowableTransformerManager_with_only_specialised_transformers_will_only_return_true_for_known_methods() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        Method someStringMethod = InnerClass.class.getMethod("someString");

        Map<Method, ThrowableTransformer> throwableTransformers = new HashMap<>();
        throwableTransformers.put(getMethod, throwable -> true);
        ThrowableTransformerManager throwableTransformerManager = new ThrowableTransformerManager(throwableTransformers);

        assertTrue(throwableTransformerManager.transformationIsRequired(getMethod));
        assertFalse(throwableTransformerManager.transformationIsRequired(someStringMethod));
    }

    @Test
    void trying_to_transform_throwable_for_a_method_that_does_not_have_a_transformer_throws_MissingTransformerException() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        Method someStringMethod = InnerClass.class.getMethod("someString");

        Map<Method, ThrowableTransformer> throwableTransformers = new HashMap<>();
        throwableTransformers.put(getMethod, throwable -> true);
        ThrowableTransformerManager throwableTransformerManager = new ThrowableTransformerManager(throwableTransformers);

        assertDoesNotThrow(() -> throwableTransformerManager.transform(getMethod, new RuntimeException()));
        MissingTransformerException missingTransformerException = assertThrows(MissingTransformerException.class, () -> throwableTransformerManager.transform(someStringMethod, new RuntimeException()));
        assertTrue(missingTransformerException.getMessage().contains(someStringMethod.getName()));
    }

    @Test
    void transform_will_use_default_transformer_if_no_specialised_transformer_was_found() throws NoSuchMethodException {
        Method getMethod = InnerClass.class.getMethod("get");
        ThrowableTransformerManager throwableTransformerManager = new ThrowableTransformerManager(throwable -> true);
        assertTrue((boolean) throwableTransformerManager.transform(getMethod, new RuntimeException()));
    }

    private static class InnerClass {

        public boolean get() {
            return false;
        }

        public String someString() {
            return "some string";
        }
    }
}