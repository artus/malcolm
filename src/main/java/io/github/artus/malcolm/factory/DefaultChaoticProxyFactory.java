package io.github.artus.malcolm.factory;

import io.github.artus.malcolm.ChaoticInvocationHandler;

import java.lang.reflect.Proxy;

public class DefaultChaoticProxyFactory implements ChaoticProxyFactory {

    @Override
    @SuppressWarnings("unchecked")
    public <InterfaceType, ImplementingType extends InterfaceType> InterfaceType getChaoticProxyClass(ImplementingType target, Class<? extends InterfaceType> interfaceTypeClass) {
        return (InterfaceType) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class[] { interfaceTypeClass },
                new ChaoticInvocationHandler(target)
        );
    }
}