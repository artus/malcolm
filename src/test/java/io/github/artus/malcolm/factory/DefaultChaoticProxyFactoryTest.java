package io.github.artus.malcolm.factory;

import io.github.artus.malcolm.exceptions.ChaoticException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DefaultChaoticProxyFactoryTest {

    @Test
    void getChaoticProxyClass_does_not_throw_Exception() {
        assertDoesNotThrow(() -> {
            InnerClass concreteClass = new InnerClass();
            new DefaultChaoticProxyFactory().getChaoticProxyClass(concreteClass, InnerInterface.class);
        });
    }

    @Test
    void getChaoticProxyClass_provides_proxied_class_that_fails_with_fifty_percent_probability() {

        InnerClass concreteClass = new InnerClass();
        InnerInterface proxiedclass = new DefaultChaoticProxyFactory().getChaoticProxyClass(concreteClass, InnerInterface.class);

        int failures = 0;
        int count = 0;

        for (int i = 0; i < 1000000; i++) {
            try {
                boolean success = proxiedclass.get();
            } catch (ChaoticException e) {
                failures++;
            }
            count++;
        }

        double average = (double) failures / (double) count;

        assertTrue(average < 0.51);
        assertTrue(average > 0.49);
    }

    private interface InnerInterface {
        boolean get();
    }

    private static class InnerClass implements InnerInterface {
        @Override
        public boolean get() {
            return true;
        }
    }
}