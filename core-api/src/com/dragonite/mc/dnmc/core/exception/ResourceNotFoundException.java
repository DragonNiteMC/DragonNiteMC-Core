package com.dragonite.mc.dnmc.core.exception;

import com.dragonite.mc.dnmc.core.managers.ResourceManager;

/**
 * @see ResourceManager
 */
public class ResourceNotFoundException extends Exception {
    private final String plugin;

    public ResourceNotFoundException(String plugin) {
        super("找不到插件 "+plugin+" 的遠端資源");
        this.plugin = plugin;
    }

    public String getPlugin() {
        return plugin;
    }
}
