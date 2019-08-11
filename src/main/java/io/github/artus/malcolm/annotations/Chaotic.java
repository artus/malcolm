package io.github.artus.malcolm.annotations;

import io.github.artus.malcolm.factory.ChaoticProxyFactory;
import io.github.artus.malcolm.factory.DefaultChaoticProxyFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Chaotic {
    Class<? extends ChaoticProxyFactory> factory() default DefaultChaoticProxyFactory.class;
}
