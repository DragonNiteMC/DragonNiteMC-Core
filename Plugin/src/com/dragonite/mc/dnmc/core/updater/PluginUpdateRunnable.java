package com.dragonite.mc.dnmc.core.updater;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

abstract class PluginUpdateRunnable extends BukkitRunnable {

    private final String plugin;
    private final Consumer<String> afterRun;
    private final Consumer<Exception> errorRun;

    protected PluginUpdateRunnable(String plugin, Consumer<String> afterRun, Consumer<Exception> errorRun) {
        this.plugin = plugin;
        this.afterRun = afterRun;
        this.errorRun = errorRun;
    }

    @Override
    public void run() {
        try {
            afterRun.accept(execute(plugin));
        } catch (Exception e) {
            errorRun.accept(e);
        }
    }

    public abstract String execute(String plugin) throws Exception;
}
