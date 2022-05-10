package com.dragonnite.mc.dnmc.core.listener;

import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventBus;
import net.luckperms.api.event.user.UserDataRecalculateEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class UpdateFormatListener {

    private Plugin plugin;
    private LuckPerms luckPermsapi;

    public UpdateFormatListener(Plugin plugin, LuckPerms luckPermsapi) {
        this.plugin = plugin;
        this.luckPermsapi = luckPermsapi;
    }

    public void register() {
        EventBus eventBus = luckPermsapi.getEventBus();
        eventBus.subscribe(this.plugin, UserDataRecalculateEvent.class, e -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                DragonNiteMC.getAPI().getNameTagManager().updatePlayer(player);
                DragonNiteMC.getAPI().getChatFormatManager().updatePlayerList(player);
            });
        });
    }



}
