package com.dragonite.mc.dnmc.core.chatformat;

import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.managers.ChatFormatManager;
import com.dragonite.mc.dnmc.core.managers.NameTagManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.UUID;

public class NameTagHandler implements NameTagManager {
    private final HashMap<UUID, Team> nametag = new HashMap<>();
    private Scoreboard scoreboard;
    @Inject
    private ChatFormatManager format;

    public NameTagHandler() {
    }

    public void setup() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    }

    public void addPlayer(Player player) {
        if (nametag.containsKey(player.getUniqueId())) return;
        Team t = scoreboard.getTeam(player.getName());
        Team team = t != null ? t : scoreboard.registerNewTeam(player.getName());
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.addEntry(player.getName());
        if (setTagPrefixSuffix(player, team)) return;
        player.setScoreboard(scoreboard);
        nametag.put(player.getUniqueId(), team);
    }

    public void updatePlayer(Player player) {
        if (!nametag.containsKey(player.getUniqueId())) return;
        Team team = nametag.get(player.getUniqueId());
        if (setTagPrefixSuffix(player, team)) return;
        nametag.put(player.getUniqueId(), team);
    }

    private boolean setTagPrefixSuffix(Player player, Team team) {
        String[] formats = format.getFormat(player).split(player.getDisplayName());
        if (formats.length != 2) return true;
        team.setPrefix(formats[0]);
        team.setSuffix(formats[1]);
        return false;
    }
}
