package io.github.artus.transformers;

public class BooleanThrowableTransformer implements ThrowableTransformer<Boolean> {
    @Override
    public Boolean transform(Throwable throwable) {
        return false;
    }
}
