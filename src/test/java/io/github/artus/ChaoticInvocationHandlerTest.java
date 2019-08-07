package io.github.artus;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

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

        assertNotNull(chaoticInvocationHandler.getThrowableSupplier());
        assertTrue(chaoticInvocationHandler.getThrowableSupplier() instanceof RuntimeExceptionSupplier);
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

    private static class InnerClass implements InnerInterface{
        @Override
        public void execute() {
        }
    }

    private interface InnerInterface {
        void execute();
    }

}