package com.dragonnite.mc.dnmc.core.ericlam;

import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatRunnerHandler implements Listener {
    private final Map<UUID, Consumer<Player>> runnerMap = new HashMap<>();
    private final Map<UUID, Integer> clickTimes = new HashMap<>();
    private final Map<UUID, Integer> maxClickTimes = new HashMap<>();


    public void registerClicks(UUID id, Consumer<Player> runner, int clicks) {
        runnerMap.put(id, runner);
        maxClickTimes.put(id, clicks);
    }


    public void registerTimeout(UUID id, Consumer<Player> runner, int timeout) {
        runnerMap.put(id, runner);
        Bukkit.getScheduler().scheduleSyncDelayedTask(DragonNiteMC.plugin, () -> runnerMap.remove(id), timeout * 20L);
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
        if (!e.getMessage().startsWith("/command-run")) return;
        String[] params = e.getMessage().split("_");
        if (params.length != 2) return;
        e.setCancelled(true);
        UUID uuid;
        try {
            uuid = UUID.fromString(params[1]);
        } catch (IllegalArgumentException ex) {
            return;
        }
        Consumer<Player> runner = runnerMap.get(uuid);
        if (runner == null) {
            e.getPlayer().sendMessage("§c文字點擊已過期。");
            return;
        }
        runner.accept(e.getPlayer());
        if (!maxClickTimes.containsKey(uuid)) return;
        clickTimes.putIfAbsent(uuid, 0);
        clickTimes.computeIfPresent(uuid, (uuid1, integer) -> ++integer);
        if (clickTimes.get(uuid) >= maxClickTimes.get(uuid)) {
            runnerMap.remove(uuid);
            clickTimes.remove(uuid);
            maxClickTimes.remove(uuid);
        }
    }

}
