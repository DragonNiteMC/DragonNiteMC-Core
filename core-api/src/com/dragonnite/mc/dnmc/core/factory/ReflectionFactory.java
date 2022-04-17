package com.dragonnite.mc.dnmc.core.factory;

import java.util.Optional;

public interface ReflectionFactory {

    /**
     * @param constructorParams 建構子參數
     * @return this
     */
    ReflectionFactory setConstructor(Object... constructorParams);

    /**
     * @param methodName 方法名稱
     * @param parameters 參數
     * @return this
     */
    ReflectionFactory addMethod(String methodName, Object... parameters);

    /**
     * @param methodName static 方法名稱
     * @param parameters 參數
     * @return this
     */
    ReflectionFactory addStaticMethod(String methodName, Object... parameters);

    /**
     * @return Optional MethodWrapper
     */
    Optional<MethodWrapper> getMethods();

    /**
     * @param exception Exception class
     * @param <E>       Exception Type
     * @return Optional MethodWrapper
     * @throws E Throwable Type
     */
    <E extends Throwable> Optional<MethodWrapper> getMethodsWithException(Class<E> exception) throws E;

    /**
     * @return Optional MethodWrapper
     */
    Optional<MethodWrapper> getStaticMethods();

    /**
     * @return MethodWrapper
     * @throws ClassNotFoundException 找不到 class
     */
    MethodWrapper getStaticMethodsWithException() throws ClassNotFoundException;

}
