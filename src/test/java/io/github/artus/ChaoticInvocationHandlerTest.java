package io.github.artus;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ChaoticInvocationHandlerTest {

    @Test
    void instantiating_ChaoticInvocationHandler_with_only_target_parameter_creates_default_DecisionMaker() {
        ChaoticInvocationHandler chaoticInvocationHandler = new ChaoticInvocationHandler(new InnerClass());

        assertNotNull(chaoticInvocationHandler.getDecisionMaker());
        assertTrue(chaoticInvocationHandler.getDecisionMaker() instanceof RandomDecisionMaker);
    }

    @Test
    void instantiating_ChaoticInvocationHandler_with_only_target_parameter_creates_default_ThrowableSupplier() {
        ChaoticInvocationHandler chaoticInvocationHandler = new ChaoticInvocationHandler(new InnerClass());

        assertNotNull(chaoticInvocationHandler.getDefaultThrowableSupplier());
        assertTrue(chaoticInvocationHandler.getDefaultThrowableSupplier() instanceof RuntimeExceptionSupplier);
    }

    @Test
    void using_ChaoticInvocationHandler_correctly_handles_proxied_methods() {

        InnerClass innerClass = new InnerClass();
        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker(0);

        InnerInterface proxiedClass = (InnerInterface) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[] { InnerInterface.class },
                new ChaoticInvocationHandler(innerClass, randomDecisionMaker)
        );

        for (int i = 0; i < 1000000; i++) {
            assertDoesNotThrow(proxiedClass::execute);
        }
        randomDecisionMaker.setProbability(1);
        for (int i = 0; i < 1000000; i++) {
            assertThrows(RuntimeException.class, proxiedClass::execute);
        }
    }

    @Test
    void when_no_ThrowableTransformersManager_is_supplied_Throwables_are_not_transformed() {
        InnerClass innerClass = new InnerClass();
        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker(1);

        InnerInterface proxiedClass = (InnerInterface) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[] { InnerInterface.class },
                new ChaoticInvocationHandler(innerClass, randomDecisionMaker)
        );

        assertThrows(RuntimeException.class, proxiedClass::get);
        assertThrows(RuntimeException.class, () -> {
            String toString = proxiedClass.someString();
        });
    }

    @Test
    void ThrowableTransformers_get_correctly_used_when_required() throws NoSuchMethodException {
        InnerClass innerClass = new InnerClass();
        RandomDecisionMaker randomDecisionMaker = new RandomDecisionMaker(1);

        Map<Method, ThrowableTransformer> throwableTransformers = new HashMap<>();
        Method getMethod = InnerInterface.class.getMethod("get");
        throwableTransformers.put(getMethod, new BooleanThrowableTransformer());

        ThrowableTransformer defaultTransformer = throwable -> "transformed";

        ThrowableTransformerManager throwableTransformerManager = new ThrowableTransformerManager(defaultTransformer, throwableTransformers);

        InnerInterface proxiedClass = (InnerInterface) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[] { InnerInterface.class },
                new ChaoticInvocationHandler(innerClass, randomDecisionMaker, throwableTransformerManager)
        );

        assertFalse(proxiedClass.get());
        assertEquals(proxiedClass.someString(), "transformed");
    }

    private static class InnerClass implements InnerInterface{
        @Override
        public void execute() {
        }

        @Override
        public boolean get() {
            return true;
        }

        @Override
        public String someString() {
            return "not transformed";
        }
    }

    private interface InnerInterface {
        void execute();
        boolean get();
        String someString();
    }

}