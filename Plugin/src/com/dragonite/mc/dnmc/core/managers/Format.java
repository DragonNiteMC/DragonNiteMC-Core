package com.dragonite.mc.dnmc.core.managers;

import com.dragonite.mc.dnmc.core.chatformat.ChatFormat;
import com.dragonite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import me.clip.placeholderapi.PlaceholderAPI;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class Format implements ChatFormatManager {
    private HashMap<String, ChatFormat> formats;

    @Inject
    private FormatDatabaseManager format;

    private LuckPerms luckPermsApi;

    public Format() {
    }

    public static <T> T noNull(T t, T e) {
        return Optional.ofNullable(t).orElse(e);
    }

    public void setup(LuckPerms luckPermsApi) {
        this.luckPermsApi = luckPermsApi;
        this.formats = format.getMap();
    }

    @Nullable
    private String getPrimaryGroup(Set<String> groups) {
        Comparator<String> comparator = Collections.reverseOrder((pg, g) -> {
            var pgPriority = Optional.ofNullable(formats.get(pg)).map(ChatFormat::getPriority).orElse(0);
            var gPriority = Optional.ofNullable(formats.get(g)).map(ChatFormat::getPriority).orElse(0);
            return Integer.compare(pgPriority, gPriority);
        });
        return groups.stream().min(comparator).orElse(null);
    }

    @Override
    public String getPlayerPrefix(Player player) {
        User user = this.luckPermsApi.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        return user.getCachedData().getMetaData().getPrefix();
    }

    @Override
    public String getPlayerSuffix(Player player) {
        User user = this.luckPermsApi.getUserManager().getUser(player.getUniqueId());
        if (user == null) return "";
        return user.getCachedData().getMetaData().getSuffix();
    }

    @Override
    public String getChatFormat(Player player) {
        var user = this.luckPermsApi.getUserManager().getUser(player.getUniqueId());
        final boolean papiEnabled = DragoniteMC.getDnmCoreConfig().isPapiEnabled();

        Set<String> groups = user.getNodes().stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName)
                .collect(Collectors.toSet());
        String primaryGroup = getPrimaryGroup(groups);

        if (primaryGroup == null || !formats.containsKey(primaryGroup)) {
            if (formats.containsKey("Player")) primaryGroup = "Player";
            else return ""; //if null, use back normal format
        }

        String gamestats = DragoniteMC.getDnmCoreConfig().getGameStats();

        ImmutableContextSet contexts = this.luckPermsApi.getContextManager().getContext(player);
        Group group = this.luckPermsApi.getGroupManager().getGroup(primaryGroup);
        CachedDataManager groupData = group != null ? group.getCachedData() : null;

        String gprefix = "";
        String gsuffix = "";

        if (groupData != null) {
            gprefix = groupData.getMetaData(QueryOptions.contextual(contexts)).getPrefix();
            gsuffix = groupData.getMetaData(QueryOptions.contextual(contexts)).getSuffix();
        }

        String prefix = user.getCachedData().getMetaData(QueryOptions.contextual(contexts)).getPrefix();
        String suffix = user.getCachedData().getMetaData(QueryOptions.contextual(contexts)).getSuffix();

        String finalGprefix = gprefix;
        String finalGsuffix = gsuffix;

        final String msg = Optional.ofNullable(formats.get(primaryGroup)).map(format -> format.getChatformat()
                .replace("<game-stats>", gamestats)
                .replace("<prefix>", noNull(prefix, ""))
                .replace("<suffix>", noNull(suffix, ""))
                .replace("<g-prefix>", noNull(finalGprefix, ""))
                .replace("<g-suffix>", noNull(finalGsuffix, ""))
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
