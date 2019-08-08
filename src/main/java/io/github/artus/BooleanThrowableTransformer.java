package io.github.artus;

public class BooleanThrowableTransformer implements ThrowableTransformer<Boolean> {
    @Override
    public Boolean transform(Throwable throwable) {
        return false;
    }
}
