package com.dragonite.mc.dnmc.core.managers;

import org.bukkit.scheduler.BukkitTask;

/**
 * 由 API 方使用的 Scheduler
 *
 * @see org.bukkit.scheduler.BukkitScheduler
 */
public interface CoreScheduler {

    BukkitTask runAsync(Runnable runnable);

    BukkitTask runAsyncLater(Runnable runnable, long ticks);

    BukkitTask runTask(Runnable runnable);

    BukkitTask runTaskLater(Runnable runnable, long ticks);

}
