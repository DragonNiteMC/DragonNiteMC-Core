package com.dragonite.mc.dnmc.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
    This class is copied from web
 */

/**
 * Java Reflection Tools
 */
public class ReflectionUtil {

    /*
     * The server version string to location NMS & OBC classes
     */
    private static String versionString;

    /*
     * Cache of NMS classes that we've searched for
     */
    private static Map<String, Class<?>> loadedNMSClasses = new HashMap<String, Class<?>>();

    /*
     * Cache of OBS classes that we've searched for
     */
    private static Map<String, Class<?>> loadedOBCClasses = new HashMap<String, Class<?>>();

    /*
     * Cache of methods that we've found in particular classes
     */
    private static Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<Class<?>, Map<String, Method>>();

    /*
     * Cache of fields that we've found in particular classes
     */
    private static Map<Class<?>, Map<String, Field>> loadedFields = new HashMap<Class<?>, Map<String, Field>>();

    /**
     * Gets the version string for NMS and OBC class paths
     *
     * @return The version string of OBC and NMS packages
     */
    public static String getVersion() {
        if (versionString == null) {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
        }

        return versionString;
    }

    /**
     * Get an NMS Class
     *
     * @param nmsClassName The name of the class
     * @return The class
     */
    public static Optional<Class<?>> getNMSClass(String nmsClassName) {
        if (loadedNMSClasses.containsKey(nmsClassName)) {
            return Optional.ofNullable(loadedNMSClasses.get(nmsClassName));
        }
        String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            return Optional.ofNullable(loadedNMSClasses.put(nmsClassName, null));
        }

        loadedNMSClasses.put(nmsClassName, clazz);
        return Optional.ofNullable(clazz);
    }

    /**
     * Get a class from the org.bukkit.craftbukkit package
     *
     * @param obcClassName the path to the class
     * @return the found class at the specified path
     */
    public synchronized static Optional<Class<?>> getOBCClass(String obcClassName) {
        if (loadedOBCClasses.containsKey(obcClassName)) {
            return Optional.of(loadedOBCClasses.get(obcClassName));
        }

        String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
        Class<?> clazz;

        try {
            clazz = Class.forName(clazzName);
        } catch (Throwable t) {
            t.printStackTrace();
            loadedOBCClasses.put(obcClassName, null);
            return Optional.empty();
        }

        loadedOBCClasses.put(obcClassName, clazz);
        return Optional.ofNullable(clazz);
    }

    /**
     * Get a Bukkit {@link Player} players NMS playerConnection object
     *
     * @param player The player
     * @return The players connection
     */
    public static Optional<Object> getConnection(Player player) {
        Optional<Method> getHandleMethod = getMethod(player.getClass(), "getHandle");

        if (getHandleMethod.isPresent()) {
            try {
                Object nmsPlayer = getHandleMethod.get().invoke(player);
                Field playerConField = getField(nmsPlayer.getClass(), "playerConnection").orElseThrow();
                return Optional.ofNullable(playerConField.get(nmsPlayer));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /**
     * Get a classes constructor
     *
     * @param clazz  The constructor class
     * @param params The parameters in the constructor
     * @return The constructor object
     */
    public static Optional<Constructor<?>> getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return Optional.ofNullable(clazz.getConstructor(params));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    /**
     * Get a method from a class that has the specific paramaters
     *
     * @param clazz      The class we are searching
     * @param methodName The name of the method
     * @return The method with appropriate paramaters
     */
    public static Optional<Method> getMethod(Class<?> clazz, String methodName) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<String, Method>());
        }

        Map<String, Method> methods = loadedMethods.get(clazz);

        if (methods.containsKey(methodName)) {
            return Optional.ofNullable(methods.get(methodName));
        }

        try {
            Method method = clazz.getMethod(methodName);
            methods.put(methodName, method);
            loadedMethods.put(clazz, methods);
            return Optional.ofNullable(method);
        } catch (Exception e) {
            e.printStackTrace();
            methods.put(methodName, null);
            loadedMethods.put(clazz, methods);
            return Optional.empty();
        }
    }

    /**
     * Get a method from a class that has the specific paramaters
     *
     * @param clazz      The class we are searching
     * @param methodName The name of the method
     * @param params     Any parameters that the method has
     * @return The method with appropriate paramaters
     */
    public static Optional<Method> getMethod(Class<?> clazz, String methodName, Class<?>... params) {
        if (!loadedMethods.containsKey(clazz)) {
            loadedMethods.put(clazz, new HashMap<String, Method>());
        }

        Map<String, Method> methods = loadedMethods.get(clazz);

        if (methods.containsKey(methodName)) {
            return Optional.ofNullable(methods.get(methodName));
        }

        try {
            Method method = clazz.getMethod(methodName, params);
            methods.put(methodName, method);
            loadedMethods.put(clazz, methods);
            return Optional.ofNullable(method);
        } catch (Exception e) {
            e.printStackTrace();
            methods.put(methodName, null);
            loadedMethods.put(clazz, methods);
            return Optional.empty();
        }
    }

    /**
     * Get a field with a particular name from a class
     *
     * @param clazz     The class
     * @param fieldName The name of the field
     * @return The field object
     */
    public static Optional<Field> getField(Class<?> clazz, String fieldName) {
        if (!loadedFields.containsKey(clazz)) {
            loadedFields.put(clazz, new HashMap<String, Field>());
        }

        Map<String, Field> fields = loadedFields.get(clazz);

        if (fields.containsKey(fieldName)) {
            return Optional.ofNullable(fields.get(fieldName));
        }

        try {
            Field field = clazz.getField(fieldName);
            fields.put(fieldName, field);
            loadedFields.put(clazz, fields);
            return Optional.ofNullable(field);
        } catch (Exception e) {
            e.printStackTrace();
            fields.put(fieldName, null);
            loadedFields.put(clazz, fields);
            return Optional.empty();
        }
    }
}
