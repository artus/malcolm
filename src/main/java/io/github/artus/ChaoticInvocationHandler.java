package io.github.artus;

import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Getter
public class ChaoticInvocationHandler implements InvocationHandler {

    private final DecisionMaker decisionMaker;
    private final ThrowableSupplier defaultThrowableSupplier;
    private final Object target;
    private final ThrowableTransformerManager throwableTransformerManager;

    public ChaoticInvocationHandler(Object target) {
        this(target, new RandomDecisionMaker());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker) {
        this(target, decisionMaker, new RuntimeExceptionSupplier());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableSupplier defaultThrowableSupplier) {
        this(target, decisionMaker, defaultThrowableSupplier, null);
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableTransformerManager throwableTransformerManager) {
        this(target, decisionMaker, new RuntimeExceptionSupplier(), throwableTransformerManager);
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableSupplier defaultThrowableSupplier, ThrowableTransformerManager throwableTransformerManager) {
        this.decisionMaker = decisionMaker;
        this.defaultThrowableSupplier = defaultThrowableSupplier;
        this.target = target;
        this.throwableTransformerManager = throwableTransformerManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.getDecisionMaker().decide()) {
            Throwable throwable = this.getDefaultThrowableSupplier().next();
            if (this.transformationIsRequired(method)) {
                return this.getThrowableTransformerManager().transform(method, throwable);
            }
            throw throwable;
        } else {
            return method.invoke(this.getTarget(), args);
        }
    }

    private boolean transformationIsRequired(Method method) {
        return this.getThrowableTransformerManager() != null
                && this.getThrowableTransformerManager().transformationIsRequired(method);
    }
}
