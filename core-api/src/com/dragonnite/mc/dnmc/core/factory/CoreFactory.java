package com.dragonnite.mc.dnmc.core.factory;

import com.dragonnite.mc.dnmc.core.config.ConfigFactory;
import com.dragonnite.mc.dnmc.core.managers.builder.Builder;
import org.bukkit.plugin.Plugin;

public interface CoreFactory {

    /**
     * @param className 連帶 package 的 Class 名稱
     * @return 反射工廠
     */
    ReflectionFactory getReflectionFactory(final String className);

    /**
     * @param plugin 需要創建文件的插件實例
     * @return 文件工廠
     */
    ConfigFactory getConfigFactory(Plugin plugin);

    /**
     * @return 建造工廠
     */
    Builder getBuilder();

}
