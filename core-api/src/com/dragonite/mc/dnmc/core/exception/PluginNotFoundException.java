package com.dragonite.mc.dnmc.core.exception;

import com.dragonite.mc.dnmc.core.managers.ResourceManager;

/**
 * @see ResourceManager
 */
public class PluginNotFoundException extends Exception{
    private final String plugin;

    public PluginNotFoundException(String plugin) {
        super("找不到插件 "+plugin);
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }
}
