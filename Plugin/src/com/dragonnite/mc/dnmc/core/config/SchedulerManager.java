package com.dragonnite.mc.dnmc.core.config;

import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.managers.CoreScheduler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerManager implements CoreScheduler {

    @Inject
    private Plugin plugin;

    @Override
    public BukkitTask runAsync(Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public BukkitTask runAsyncLater(Runnable runnable, long ticks) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, ticks);
    }

    @Override
    public BukkitTask runTask(Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public BukkitTask runTaskLater(Runnable runnable, long ticks) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, ticks);
    }
}
