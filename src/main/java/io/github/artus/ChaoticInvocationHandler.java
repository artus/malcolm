package io.github.artus;

import io.github.artus.decisionmakers.DecisionMaker;
import io.github.artus.decisionmakers.ProbabilityBasedDecisionMaker;
import io.github.artus.managers.ThrowableSupplierManager;
import io.github.artus.managers.ThrowableTransformerManager;
import io.github.artus.suppliers.RuntimeExceptionSupplier;
import lombok.Getter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Getter
public class ChaoticInvocationHandler implements InvocationHandler {

    private final DecisionMaker decisionMaker;
    private final ThrowableSupplierManager throwableSupplierManager;
    private final Object target;
    private final ThrowableTransformerManager throwableTransformerManager;

    public ChaoticInvocationHandler(Object target) {
        this(target, new ProbabilityBasedDecisionMaker());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker) {
        this(target, decisionMaker, new ThrowableSupplierManager(new RuntimeExceptionSupplier()));
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableSupplierManager throwableSupplierManager) {
        this(target, decisionMaker, throwableSupplierManager, null);
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableTransformerManager throwableTransformerManager) {
        this(target, decisionMaker, new ThrowableSupplierManager(new RuntimeExceptionSupplier()), throwableTransformerManager);
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableSupplierManager throwableSupplierManager, ThrowableTransformerManager throwableTransformerManager) {
        this.decisionMaker = decisionMaker;
        this.throwableSupplierManager = throwableSupplierManager;
        this.target = target;
        this.throwableTransformerManager = throwableTransformerManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.interventionIsRequired(method)) {
            Throwable throwable = this.getThrowableSupplierManager().supply(method);
            if (this.transformationIsRequired(method)) {
                return this.getThrowableTransformerManager().transform(method, throwable);
            }
            throw throwable;
        } else {
            return method.invoke(this.getTarget(), args);
        }
    }

    private boolean interventionIsRequired(Method method) {
        return this.getThrowableSupplierManager().interventionIsRequired(method)
                && this.getDecisionMaker().decide();
    }

    private boolean transformationIsRequired(Method method) {
        return this.getThrowableTransformerManager() != null
                && this.getThrowableTransformerManager().transformationIsRequired(method);
    }
}
