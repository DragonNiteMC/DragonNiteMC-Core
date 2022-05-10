package com.dragonite.mc.dnmc.core.factory;

import com.dragonite.mc.dnmc.core.config.ConfigBuilder;
import com.dragonite.mc.dnmc.core.config.ConfigFactory;
import com.dragonite.mc.dnmc.core.managers.builder.Builder;
import com.google.inject.Inject;
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
