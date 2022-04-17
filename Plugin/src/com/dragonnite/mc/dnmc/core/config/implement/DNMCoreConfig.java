package com.dragonnite.mc.dnmc.core.config.implement;

import com.dragonnite.mc.dnmc.core.config.implement.yaml.*;
import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.factory.CoreFactory;
import com.dragonnite.mc.dnmc.core.managers.CoreConfig;
import com.dragonnite.mc.dnmc.core.managers.YamlManager;
import org.bukkit.plugin.Plugin;

public class DNMCoreConfig implements CoreConfig {
    private final YamlManager manager;
    private final boolean papiEnabled;


    @Inject
    public DNMCoreConfig(Plugin plugin, CoreFactory factory) { //real constructor
        this.manager = factory.getConfigFactory(plugin)
                .register(DatabaseConfig.class)
                .register(MainConfig.class)
                .register(CancelEventConfig.class)
                .register(MessageConfig.class)
                .register(HelpConfig.class)
                .register(VersionCheckerConfig.class)
                .dump();
        papiEnabled = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public MainConfig getConfig() {
        return manager.getConfigAs(MainConfig.class);
    }

    public DatabaseConfig getDatabase() {
        return manager.getConfigAs(DatabaseConfig.class);
    }

    public HelpConfig getHelp() {
        return manager.getConfigAs(HelpConfig.class);
    }

    public CancelEventConfig getCancel() {
        return manager.getConfigAs(CancelEventConfig.class);
    }

    public VersionCheckerConfig getVersionChecker() {
        return manager.getConfigAs(VersionCheckerConfig.class);
    }

    public boolean isPapiEnabled() {
        return papiEnabled;
    }

    public int getMostDelay() {
        return manager.getConfigAs(MainConfig.class).fallBackDelay;
    }

    public void reloadHelp() {
        getHelp().reload();
    }

    @Override
    public String getPrefix() {
        return getMessageConfig().getPrefix();
    }

    @Override
    public String getNoPerm() {
        return getMessageConfig().getPure("No-Perm");
    }

    @Override
    public String getNotPlayer() {
        return getMessageConfig().getPure("Not-Player");
    }

    @Override
    public String getNotFoundPlayer() {
        return getMessageConfig().getPure("Player-Not-Found");
    }

    public MessageConfig getMessageConfig() {
        return manager.getConfigAs(MessageConfig.class);
    }

    public String getGameStats() {
        return getDatabase().chatFormatPlaceholder;
    }
}
