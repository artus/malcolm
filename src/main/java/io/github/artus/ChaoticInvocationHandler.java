package io.github.artus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ChaoticInvocationHandler implements InvocationHandler {

    private DecisionMaker decisionMaker;
    private ThrowableSupplier throwableSupplier;
    private Object target;

    public ChaoticInvocationHandler(Object target) {
        this(target, new RandomDecisionMaker());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker) {
        this(target, decisionMaker, new RuntimeExceptionSupplier());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableSupplier throwableSupplier) {
        this.setDecisionMaker(decisionMaker);
        this.setThrowableSupplier(throwableSupplier);
        this.setTarget(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.getDecisionMaker().decide()) {
            throw this.getThrowableSupplier().next();
        } else {
            return method.invoke(this.getTarget(), args);
        }
    }
}
