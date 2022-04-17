package com.dragonnite.mc.dnmc.core.bungeecord;

import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class StopRunnable extends BukkitRunnable {

    private final boolean restart;
    private int delay;

    StopRunnable(boolean restart) {
        this.restart = restart;
        DragonNiteMC.plugin.getLogger().info("Waiting for all players to fallback lobby...");
    }

    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() <= 0 || delay >= DragonNiteMC.getDnmCoreConfig().getMostDelay() * 20) {

            if (restart) Bukkit.spigot().restart();
            else Bukkit.shutdown();

            this.cancel();
        }

        ++delay;
    }
}
