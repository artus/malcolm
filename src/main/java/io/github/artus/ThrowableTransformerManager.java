package io.github.artus;

import lombok.AccessLevel;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Getter(AccessLevel.PRIVATE)
public class ThrowableTransformerManager {

    private final Map<Method, ThrowableTransformer> transformers;
    private final ThrowableTransformer defaultTransformer;

    public ThrowableTransformerManager(ThrowableTransformer defaultTransformer) {
        this(defaultTransformer, new HashMap<>());
    }

    public ThrowableTransformerManager(Map<Method, ThrowableTransformer> transformers) {
        this(null, transformers);
    }

    public ThrowableTransformerManager(ThrowableTransformer defaultTransformer, Map<Method, ThrowableTransformer> transformers) {
        this.defaultTransformer = defaultTransformer;
        this.transformers = transformers;
    }

    public boolean transformationIsRequired(Method method) {
        return this.getDefaultTransformer() != null || this.getTransformers().containsKey(method);
    }

    public Object transform(Method method, Throwable throwable) {
        if (!this.transformationIsRequired(method)) {
            String message = String.format("No transformer was found for method %s", method.getName());
            throw new MissingTransformerException(message);
        }

        if (this.getTransformers().containsKey(method)) {
            return this.getTransformers().get(method).transform(throwable);
        }
        return this.getDefaultTransformer().transform(throwable);
    }
}
