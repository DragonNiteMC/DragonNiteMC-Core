package com.dragonite.mc.dnmc.core.bungeecord;

import com.dragonite.mc.dnmc.core.managers.BungeeManager;
import com.dragonite.mc.dnmc.core.managers.CoreConfig;
import com.google.common.io.ByteArrayDataOutput;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;

import static com.google.common.io.ByteStreams.newDataOutput;

public class Bungee implements BungeeManager {

    @Inject
    private CoreConfig coreConfig;

    @Inject
    private Plugin plugin;

    @Override
    public void sendPlayer(@Nonnull Player player, String server) {
        ByteArrayDataOutput out = newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendMessage(coreConfig.getPrefix() + "正在傳送你回大堂...");
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    @Override
    public void sendAllPlayers(String server) {
        new SlowIteratorRunnable<>(Bukkit.getOnlinePlayers(), player -> sendPlayer(player, server)).runTaskTimer(plugin, 0L, 2L);
    }

    @Override
    public void sendBeforeStop(String server) {
        sendAllPlayers(server);
        new StopRunnable(false).runTaskTimer(plugin, 0, 1L);
    }

    @Override
    public void sendBeforeRestart(String server) {
        sendAllPlayers(server);
        new StopRunnable(true).runTaskTimer(plugin, 0, 1L);
    }


}
