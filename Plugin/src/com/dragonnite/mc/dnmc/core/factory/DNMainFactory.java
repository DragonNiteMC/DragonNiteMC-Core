package com.dragonnite.mc.dnmc.core.factory;

import com.dragonnite.mc.dnmc.core.config.ConfigBuilder;
import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.config.ConfigFactory;
import com.dragonnite.mc.dnmc.core.managers.builder.Builder;
import org.bukkit.plugin.Plugin;

public final class DNMainFactory implements CoreFactory {

    @Inject
    private Builder builder;

    @Override
    public ReflectionFactory getReflectionFactory(final String className) {
        return new ReflectionBuilder(className);
    }

    @Override
    public ConfigFactory getConfigFactory(Plugin plugin) {
        return new ConfigBuilder(plugin);
    }

    @Override
    public Builder getBuilder() {
        return builder;
    }

}
