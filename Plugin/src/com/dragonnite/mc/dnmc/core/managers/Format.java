package com.dragonnite.mc.dnmc.core.managers;

import com.dragonnite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.chatformat.ChatFormat;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;

public class Format implements ChatFormatManager {
    private Chat chat;
    private Permission perm;
    private HashMap<String, ChatFormat> formats;


    @Inject
    private VaultAPI vaultAPI;

    @Inject
    private FormatDatabaseManager format;

    public Format() {
    }

    public static <T> T noNull(T t, T e) {
        return Optional.ofNullable(t).orElse(e);
    }

    public void setup() {
        this.chat = vaultAPI.getChat();
        this.perm = vaultAPI.getPermission();
        this.formats = format.getMap();
    }

    @Nullable
    private String getPrimaryGroup(Player player) {
        var groups = perm.getPlayerGroups(player);
        if (groups.length == 0) return null;
        Comparator<String> comparator = Collections.reverseOrder((pg, g) -> {
            var pgPriority = Optional.ofNullable(formats.get(pg)).map(ChatFormat::getPriority).orElse(0);
            var gPriority = Optional.ofNullable(formats.get(g)).map(ChatFormat::getPriority).orElse(0);
            return Integer.compare(pgPriority, gPriority);
        });
        Arrays.sort(groups, comparator);
        return groups[0];
    }

    @Override
    public String getChatFormat(Player player) {

        final boolean papiEnabled = DragonNiteMC.getDnmCoreConfig().isPapiEnabled();

        String primaryGroup = getPrimaryGroup(player);

        if (primaryGroup == null || !formats.containsKey(primaryGroup)) {
            if (formats.containsKey("Player")) primaryGroup = "Player";
            else return ""; //if null, use back normal format
        }

        String gamestats = DragonNiteMC.getDnmCoreConfig().getGameStats();

        final String pg = primaryGroup;

        final String msg = Optional.ofNullable(formats.get(primaryGroup)).map(format -> format.getChatformat()
                .replace("<game-stats>", gamestats)
                .replace("<prefix>", noNull(chat.getPlayerPrefix(player), ""))
                .replace("<suffix>", noNull(chat.getPlayerSuffix(player), ""))
                .replace("<g-prefix>", noNull(chat.getGroupPrefix(player.getWorld().getName(), pg), ""))
                .replace("<g-suffix>", noNull(chat.getGroupSuffix(player.getWorld().getName(), pg), ""))
                .replace("<player>", player.getDisplayName())).orElse("");

        final String colored = ChatColor.translateAlternateColorCodes('&', msg);
        String finalFormat;

        if (papiEnabled) finalFormat = PlaceholderAPI.setPlaceholders(player, colored);
        else finalFormat = colored;

        return finalFormat.replace("<message>", "%2$s");
    }

    @Override
    public String getFormat(Player player) {
        String format = ChatColor.translateAlternateColorCodes('&', getChatFormat(player));
        String[] list = format.split(":");
        if (list.length < 1) return "";
        return list[0];
    }

    @Override
    public void updatePlayerList(Player player) {
        String tabFormat = getFormat(player);
        if (tabFormat.isEmpty()) return;
        player.setPlayerListName(tabFormat);
    }
}
