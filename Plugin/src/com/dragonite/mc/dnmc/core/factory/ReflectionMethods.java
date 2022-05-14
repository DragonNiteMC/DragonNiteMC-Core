package com.dragonite.mc.dnmc.core.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class ReflectionMethods implements MethodWrapper {


    private final Object instance;
    private final Class<?> cls;
    private final Map<String, Object[]> methods;

    public ReflectionMethods(Object instance, Class<?> cls, Map<String, Object[]> methods) {
        this.instance = instance;
        this.cls = cls;
        this.methods = methods;
    }


    @Override
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public final <T, E extends Exception> T execute(String method, Class<T> returnType, Class<? extends E>... exceptions) throws E {
        Object[] params = methods.get(method);
        if (params == null) throw new IllegalStateException("Cannot fnd the method: " + method);
        try {
            Class<?>[] methodTypes = getParametersType(params);
            Method m = cls.getMethod(method, methodTypes);
            m.setAccessible(true);
            final Object result = m.invoke(instance, params);
            if (returnType == null) return null;
            return (T) result;
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                InvocationTargetException ex = (InvocationTargetException) e;
                final Throwable th = ex.getTargetException();
                for (Class<? extends E> exception : exceptions) {
                    try {
                        throw exception.cast(th);
                    } catch (ClassCastException ignored) {
                    }
                }
            }
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putMethod(String methodName, Object... parameters) {
        this.methods.put(methodName, parameters);
    }


    @Override
    public <T> T executeCatch(String method, Class<T> returnType, Consumer<Exception> exceptionCatch) {
        Object[] params = methods.get(method);
        if (params == null) throw new IllegalStateException("Cannot fnd the method: " + method);
        try {
            Class<?>[] methodTypes = Arrays.stream(params).map(Object::getClass).toArray(Class[]::new);
            Method m = cls.getMethod(method, methodTypes);
            m.setAccessible(true);
            final Object result = m.invoke(instance, params);
            if (returnType == null) return null;
            return returnType.cast(result);
        } catch (Exception e) {
            exceptionCatch.accept(e);
        }
        return null;
    }

}
