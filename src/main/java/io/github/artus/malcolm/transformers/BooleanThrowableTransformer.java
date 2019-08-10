package io.github.artus.malcolm.transformers;

public class BooleanThrowableTransformer implements ThrowableTransformer<Boolean> {
    @Override
    public Boolean transform(Throwable throwable) {
        return false;
    }
}
