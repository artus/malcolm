package io.github.artus.malcolm.annotations;

import io.github.artus.malcolm.factory.ChaoticProxyFactory;

import java.lang.reflect.Field;

public class MalcolmAnnotationHandler {

    public static void handle(Object target) throws InstantiationException {
        Class<?> objectClass = target.getClass();
        for (Field field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Chaotic.class)) replaceFieldWithChaoticProxy(target, field);
        }
    }

    private static void replaceFieldWithChaoticProxy(Object target, Field field) throws InstantiationException {
        try {
            Object value = field.get(target);
            Chaotic chaoticAnnotation = field.getAnnotation(Chaotic.class);
            ChaoticProxyFactory factory = chaoticAnnotation.factory().getConstructor().newInstance();
            Object proxy = factory.getChaoticProxyClass(value, field.getType());
            field.set(target, proxy);
        } catch (Exception e) {
            String errorMessage = String.format("Failed to construct ChaoticProxyFactory for field '%s'.", field.getName());
            InstantiationException exception = new InstantiationException(errorMessage);
            exception.initCause(e);
            throw exception;
        }
    }
}
