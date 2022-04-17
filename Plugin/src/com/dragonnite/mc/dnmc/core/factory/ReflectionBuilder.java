package com.dragonnite.mc.dnmc.core.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class ReflectionBuilder implements ReflectionFactory {

    private final String clsName;
    private final Map<String, Object[]> methods = new LinkedHashMap<>();
    private final Map<String, Object[]> staticMethods = new LinkedHashMap<>();
    private Object[] constructorParams;

    public ReflectionBuilder(final String clsName) {
        this.clsName = clsName;
        this.constructorParams = new Object[0];
    }

    @Override
    public ReflectionFactory setConstructor(Object... constructorParams) {
        this.constructorParams = constructorParams;
        return this;
    }

    @Override
    public ReflectionFactory addMethod(String methodName, Object... parameters) {
        this.methods.put(methodName, parameters);
        return this;
    }

    @Override
    public ReflectionFactory addStaticMethod(String methodName, Object... parameters) {
        this.staticMethods.put(methodName, parameters);
        return this;
    }

    @Override
    public Optional<MethodWrapper> getMethods() {
        try {
            Class<?> cls = Class.forName(clsName);
            Class<?>[] constructorTypes = Arrays.stream(constructorParams).map(Object::getClass).toArray(Class[]::new);
            Constructor<?> constructor = cls.getConstructor(constructorTypes);
            constructor.setAccessible(true);
            final Object instance = constructor.newInstance(constructorParams);
            return Optional.of(new ReflectionMethods(instance, cls, methods));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public <E extends Throwable> Optional<MethodWrapper> getMethodsWithException(Class<E> exception) throws E {
        try {
            Class<?> cls = Class.forName(clsName);
            Class<?>[] constructorTypes = Arrays.stream(constructorParams).map(Object::getClass).toArray(Class[]::new);
            Constructor<?> constructor = cls.getConstructor(constructorTypes);
            constructor.setAccessible(true);
            final Object instance = constructor.newInstance(constructorParams);
            return Optional.of(new ReflectionMethods(instance, cls, methods));
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException ex = (InvocationTargetException) e;
                if (ex.getTargetException().getClass() == exception) {
                    throw (E) ex.getTargetException();
                }
            } else {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<MethodWrapper> getStaticMethods() {
        try {
            Class<?> cls = Class.forName(clsName);
            return Optional.of(new ReflectionStaticMethods(cls, staticMethods));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public MethodWrapper getStaticMethodsWithException() throws ClassNotFoundException {
        Class<?> cls = Class.forName(clsName);
        return new ReflectionStaticMethods(cls, staticMethods);
    }


}
