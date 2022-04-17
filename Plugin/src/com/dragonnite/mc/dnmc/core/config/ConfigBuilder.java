package com.dragonnite.mc.dnmc.core.config;

import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;
import com.dragonnite.mc.dnmc.core.config.yaml.Resource;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ConfigBuilder implements ConfigFactory {

    private final Map<String, Class<? extends Configuration>> ymls = new HashMap<>();
    private final Plugin plugin;

    public ConfigBuilder(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public ConfigFactory register(String yml, Class<? extends Configuration> configClass) {
        this.ymls.put(yml, configClass);
        return this;
    }

    @Override
    public ConfigFactory register(Class<? extends Configuration> configClass) {
        Resource res = configClass.getAnnotation(Resource.class);
        if (res == null) throw new IllegalStateException("缺少 @Resource 標註");
        this.ymls.put(res.locate(), configClass);
        return this;
    }

    @Override
    public YamlManager dump() {
        return new YamlHandler(ymls, plugin);
    }
}
