package io.github.artus.malcolm.annotations;

import io.github.artus.malcolm.exceptions.ChaoticException;
import io.github.artus.malcolm.factory.ChaoticProxyFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MalcolmAnnotationHandlerTest {

    @Test
    void fields_marked_Chaotic_will_be_replaced_by_proxied_Class() throws InstantiationException {
        ClassUsingInnerClass classContainingChaoticProperty = new ClassUsingInnerClass();
        MalcolmAnnotationHandler.handle(classContainingChaoticProperty);

        boolean chaoticExceptionOccuredOnProxiedClass = false;
        boolean chaoticExceptionOccuredOnNotProxiedClass = false;

        int loops = 1000000;
        int count = 0;

        for (int i = 0; i < loops; i++) {
            try {
                classContainingChaoticProperty.getProxied();
            } catch (ChaoticException e) {
                chaoticExceptionOccuredOnProxiedClass = true;
                break;
            }
        }

        for (int i = 0; i < loops; i++) {
            try {
                classContainingChaoticProperty.getNotProxied();
                count++;
            } catch (ChaoticException e) {
                chaoticExceptionOccuredOnNotProxiedClass = true;
            }
        }

        assertTrue(chaoticExceptionOccuredOnProxiedClass);
        assertFalse(chaoticExceptionOccuredOnNotProxiedClass);
        assertEquals(loops, count);
    }

    @Test
    void InstantiationException_is_thrown_when_something_goes_wrong_while_inserting_proxied_classes() {
        ClassUsingBrokenFactory classUsingBrokenFactory = new ClassUsingBrokenFactory();
        InstantiationException instantiationException = assertThrows(InstantiationException.class, () -> {
            MalcolmAnnotationHandler.handle(classUsingBrokenFactory);
        });

        assertTrue(checkIfMessageIsInCauseList(ThrowingChaoticProxyFactory.CONSTRUCTOR_ERROR_MESSAGE, instantiationException));
    }

    private boolean checkIfMessageIsInCauseList(String expected, Throwable currentThrowable) {
        if (currentThrowable.getMessage() != null && currentThrowable.getMessage().equals(expected)) return true;
        if (currentThrowable.getCause() == null) return false;
        return checkIfMessageIsInCauseList(expected, currentThrowable.getCause());
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

    private static class ClassUsingInnerClass {
        @Chaotic
        private InnerInterface proxiedClass = new InnerClass();
        private InnerInterface notProxiedClass = new InnerClass();

        public boolean getProxied() {
            return this.proxiedClass.get();
        }

        public boolean getNotProxied() {
            return this.notProxiedClass.get();
        }
    }

    private static class ClassUsingBrokenFactory {
        @Chaotic(factory = ThrowingChaoticProxyFactory.class)
        private InnerInterface proxiedClass = new InnerClass();
    }

    private static class ThrowingChaoticProxyFactory implements ChaoticProxyFactory {

        public static final String CONSTRUCTOR_ERROR_MESSAGE = "This factory can not be constructed.";
        public static final String METHOD_ERROR_MESSAGE = "This factory can not create chaotic proxies.";

        public ThrowingChaoticProxyFactory() {
            throw new RuntimeException(CONSTRUCTOR_ERROR_MESSAGE);
        }

        @Override
        public <InterfaceType, ImplementingType extends InterfaceType> InterfaceType getChaoticProxyClass(ImplementingType target, Class<? extends InterfaceType> interfaceTypeClass) {
            throw new RuntimeException(METHOD_ERROR_MESSAGE);
        }
    }
}
