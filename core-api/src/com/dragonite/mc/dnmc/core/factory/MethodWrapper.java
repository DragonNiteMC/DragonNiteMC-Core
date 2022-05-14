package com.dragonite.mc.dnmc.core.factory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

public interface MethodWrapper {

    /**
     * @param method     方法名稱
     * @param returnType 返回類型
     * @param exceptions 有機會扔出的 Exception
     * @param <T>        返回類型
     * @param <E>        Exception 類型
     * @return 執行結果
     * @throws E Exception Type
     */
    <T, E extends Exception> T execute(String method, Class<T> returnType, Class<? extends E>... exceptions) throws E;

    /**
     * 修改方法的參數或新增方法
     *
     * @param methodName 方法名稱
     * @param parameters 參數
     */
    void putMethod(String methodName, Object... parameters);

    /**
     * @param method         方法名稱
     * @param returnType     返回類型
     * @param exceptionCatch Exception 處理
     * @param <T>            返回類型
     * @return 執行結果
     */
    <T> T executeCatch(String method, Class<T> returnType, Consumer<Exception> exceptionCatch);

    /**
     * @param parameters 參數
     * @return 參數類型
     */
    default Class<?>[] getParametersType(Object... parameters) {
        return Arrays.stream(parameters).map(Object::getClass).map(cls -> {
            try {
                Field field = cls.getDeclaredField("TYPE");
                field.setAccessible(true);
                return (Class<?>) field.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return cls;
            }
        }).toArray(Class[]::new);
    }
}
