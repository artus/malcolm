package io.github.artus.malcolm.managers;

import io.github.artus.malcolm.exceptions.MissingSupplierException;
import io.github.artus.malcolm.suppliers.ChaoticExceptionSupplier;
import io.github.artus.malcolm.suppliers.ThrowableSupplier;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ThrowableSupplierManager {
    private final Map<Method, ThrowableSupplier> throwableSuppliers;
    private final ThrowableSupplier defaultThrowableSupplier;

    public ThrowableSupplierManager() {
        this(new ChaoticExceptionSupplier());
    }

    public ThrowableSupplierManager(ThrowableSupplier defaultThrowableSupplier) {
        this(defaultThrowableSupplier, new HashMap<>());
    }

    public ThrowableSupplierManager(Map<Method, ThrowableSupplier> throwableSuppliers) {
        this(null, throwableSuppliers);
    }

    public ThrowableSupplierManager(ThrowableSupplier defaultThrowableSupplier, Map<Method, ThrowableSupplier> throwableSuppliers) {
        this.defaultThrowableSupplier = defaultThrowableSupplier;
        this.throwableSuppliers = throwableSuppliers;
    }

    public boolean interventionIsRequired(Method method) {
        return this.defaultThrowableSupplier != null || this.throwableSuppliers.containsKey(method);
    }

    public Throwable supply(Method method) {
        if (!this.interventionIsRequired(method)) {
            String message = String.format("No ThrowableSupplier found for method %s. Consider supplying a default ThrowableSupplier.", method.getName());
            throw new MissingSupplierException(message);
        }
        return this.throwableSuppliers.getOrDefault(method, this.defaultThrowableSupplier).supply();
    }
}
