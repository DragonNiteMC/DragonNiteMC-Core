package com.dragonite.mc.dnmc.core.listener;

import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonite.mc.dnmc.core.managers.NameTagManager;
import com.dragonite.mc.dnmc.core.managers.NickManager;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.ChatFormatManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.HashSet;
import java.util.List;

public class EventListener implements Listener {

    private DNMCoreConfig dnmCoreConfig = DragoniteMC.getDnmCoreConfig();
    @Inject
    private NameTagManager nameTagManager;
    @Inject
    private ChatFormatManager chatFormatManager;

    public EventListener() {
    }

    @EventHandler
    public void JoinHideMSG(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Player player = e.getPlayer();
        if (!dnmCoreConfig.getConfig().useOwnScoreboard) {
            chatFormatManager.updatePlayerList(player); //update player list
            nameTagManager.addPlayer(player); //update name tag
        }
    }

    //Nick Test
    @EventHandler
    public void onCommandPrepross(PlayerCommandPreprocessEvent e) {
        HashSet<Player> nicks = NickManager.getInstance().getNicks();
        nicks.forEach(nick -> e.setMessage(e.getMessage().replaceAll(nick.getDisplayName(), nick.getName())));
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent e) {
        List<String> tabs = e.getCompletions();
        for (Player nick : NickManager.getInstance().getNicks()) {
            if (tabs.contains(nick.getName())) {
                tabs.remove(nick.getName());
                tabs.add(nick.getDisplayName());
            }
        }
        e.setCompletions(tabs);
    }
    //

    @EventHandler
    public void LeaveHideMSG(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (Perm.hasPermission(event.getPlayer(), Perm.DONOR)) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
        //Nick Test
        String msg = event.getMessage();
        for (Player nick : NickManager.getInstance().getNicks()) {
            if (msg.contains(nick.getName())) {
                event.setCancelled(true);
                event.getPlayer().sendMessage("§c由於此玩家目前正在偽裝，因此你無法發送含此名字的任何訊息。");
            }
        }
    }

}
