package io.github.artus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ChaoticInvocationHandler implements InvocationHandler {

    private DecisionMaker decisionMaker;
    private ThrowableSupplier defaultThrowableSupplier;
    private Object target;
    private Map<Method, ThrowableTransformer> throwableTransformers;

    public ChaoticInvocationHandler(Object target) {
        this(target, new RandomDecisionMaker());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker) {
        this(target, decisionMaker, new RuntimeExceptionSupplier());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, ThrowableSupplier defaultThrowableSupplier) {
        this(target, decisionMaker, new HashMap<>(), defaultThrowableSupplier);
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, Map<Method, ThrowableTransformer> throwableTransformers) {
        this(target, decisionMaker, throwableTransformers, new RuntimeExceptionSupplier());
    }

    public ChaoticInvocationHandler(Object target, DecisionMaker decisionMaker, Map<Method, ThrowableTransformer> throwableTransformers, ThrowableSupplier defaultThrowableSupplier) {
        this.setDecisionMaker(decisionMaker);
        this.setDefaultThrowableSupplier(defaultThrowableSupplier);
        this.setTarget(target);
        this.setThrowableTransformers(throwableTransformers);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (this.getDecisionMaker().decide()) {
            Throwable throwable = this.getDefaultThrowableSupplier().next();
            if (this.transformationIsRequired(method)) {
                return this.getThrowableTransformers().get(method).transform(throwable);
            }
            throw throwable;
        } else {
            return method.invoke(this.getTarget(), args);
        }
    }

    private boolean transformationIsRequired(Method method) {
        return this.getThrowableTransformers().containsKey(method);
    }
}
