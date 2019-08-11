package io.github.artus.malcolm.factory;

@FunctionalInterface
public interface ChaoticProxyFactory {
    <InterfaceType, ImplementingType extends InterfaceType> InterfaceType getChaoticProxyClass(ImplementingType target, Class<? extends InterfaceType> interfaceTypeClass);
}
