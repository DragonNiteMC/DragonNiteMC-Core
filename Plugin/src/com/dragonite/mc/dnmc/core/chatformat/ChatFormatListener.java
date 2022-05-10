package com.dragonite.mc.dnmc.core.chatformat;

import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.ChatFormatManager;
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

        final boolean papiEnabled = DragoniteMC.getDnmCoreConfig().isPapiEnabled();

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
