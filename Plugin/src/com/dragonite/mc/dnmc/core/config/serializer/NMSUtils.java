package com.dragonite.mc.dnmc.core.config.serializer;

import com.dragonite.mc.dnmc.core.utils.ReflectionUtil;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;

/**
 * NMS 工具
 */
public class NMSUtils {

    public static String convertItemStackToJson(ItemStack itemStack) {
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack").orElseThrow();
        Optional<Method> asNMSCopyMethodOpt = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack").orElseThrow();
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound").orElseThrow();
        Optional<Method> saveNmsItemStackMethodOpt = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method


        try {
            Method asNMSCopyMethod = asNMSCopyMethodOpt.orElseThrow(() -> new NoSuchElementException("找不到 CraftItemStack 的 asNMSCopy 方法"));
            Method saveNmsItemStackMethod = saveNmsItemStackMethodOpt.orElseThrow(() -> new NoSuchElementException("找不到 ItemStack 的 save 方法"));
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.getConstructor().newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "ItemStack 轉換json失敗", t);
            return null;
        }

        // Return a string representation of the serialized object
        return itemAsJsonObject.toString();
    }

    public static String convertEntityToJson(Entity entity) {
        String name = entity.getName();
        UUID uuid = entity.getUniqueId();
        EntityType type = entity.getType();
        Gson gson = new Gson();
        Map<String, Object> map = new HashMap<>();
        map.put("value", Map.of("id", uuid.toString(), "type", "minecraft:" + type.toString().toLowerCase(), "name", name));
        return gson.toJson(map);
    }
}
