package io.github.artus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Random;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ChaoticInvocationHandler implements InvocationHandler {

    private Random randomNumberGenerator;
    private ThrowableSupplier throwableSupplier;

    public ChaoticInvocationHandler() {
        this(new Random());
    }

    public ChaoticInvocationHandler(Random randomNumberGenerator) {
        this(randomNumberGenerator, new RuntimeExceptionSupplier());
    }

    public ChaoticInvocationHandler(Random randomNumberGenerator, ThrowableSupplier throwableSupplier) {
        this.setRandomNumberGenerator(randomNumberGenerator);
        this.setThrowableSupplier(throwableSupplier);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {


        return null;
    }


}
