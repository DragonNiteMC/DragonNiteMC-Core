package com.dragonnite.mc.dnmc.core.chatformat;

import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.ChatFormatManager;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatListener implements Listener {

    @Inject
    private ChatFormatManager format;


    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent e) {
        Player player = e.getPlayer();

        final boolean papiEnabled = DragonNiteMC.getDnmCoreConfig().isPapiEnabled();

        final String finalMessage = player.hasPermission(Perm.DONOR) ? ChatColor.translateAlternateColorCodes('&', e.getMessage()) : e.getMessage();

        final String finalFormat = format.getChatFormat(player);

        if (finalFormat.isEmpty()) return;

        String format;

        if (papiEnabled) {
            format = PlaceholderAPI.setPlaceholders(player, finalFormat);
        } else {
            format = finalFormat;
        }

        e.setFormat(format);
        e.setMessage(finalMessage);
    }

}
