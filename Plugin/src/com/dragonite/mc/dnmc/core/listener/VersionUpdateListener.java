package com.dragonite.mc.dnmc.core.listener;

import com.dragonite.mc.dnmc.core.config.implement.yaml.VersionCheckerConfig;
import com.dragonite.mc.dnmc.core.exception.ResourceNotFoundException;
import com.dragonite.mc.dnmc.core.managers.ResourceManager;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class VersionUpdateListener implements Listener {

    private final Map<String, String> updateMap = new ConcurrentHashMap<>();

    public VersionUpdateListener(DragoniteMC dragoniteMC) {
        new CheckerRunnable(dragoniteMC).runTaskTimer(dragoniteMC, 10L, DragoniteMC.getDnmCoreConfig().getVersionChecker().intervalHours * 3600 * 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission(Perm.DEVELOPER)) return;
        updateMap.forEach((plugin, versions)->{
            var version = versions.split(":");
            e.getPlayer().sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§c 插件更新: " + plugin + " v" + version[1] + ", 最新版本: v" + version[0]);
        });
    }


    private class CheckerRunnable extends BukkitRunnable {

        private final VersionCheckerConfig config;
        private final DragoniteMC api;

        public CheckerRunnable(DragoniteMC dragoniteMC) {
            this.config = DragoniteMC.getDnmCoreConfig().getVersionChecker();
            this.api = dragoniteMC;
        }

        @Override
        public void run() {
            for (Plugin resource : Bukkit.getServer().getPluginManager().getPlugins()) {
                String plugin = resource.getName();
                if (config.resourceId_to_checks.containsKey(plugin) && config.enabled_spigot_check) {
                    api.getResourceManager(ResourceManager.Type.SPIGOT).fetchLatestVersion(plugin, v -> {
                        if (!versionNewer(resource.getDescription().getVersion(), v)){
                            updateMap.put(plugin, v+":"+resource.getDescription().getVersion());
                        }
                    }, err -> {
                        api.getLogger().warning("獲取插件 " + plugin + " 的最新版本時出現錯誤: " + err.getMessage() + " (插件資源 id 可能輸入有誤。)");
                        if (!(err instanceof IOException)) err.printStackTrace();
                    });
                } else if (!config.resourceId_to_checks.containsKey(plugin)) {
                    api.getResourceManager(ResourceManager.Type.DRAGONITE).fetchLatestVersion(plugin, v -> {
                        if (!versionNewer(resource.getDescription().getVersion(), v)){
                            updateMap.put(plugin, v+":"+resource.getDescription().getVersion());
                        }
                    }, err -> {
                        if (err instanceof ResourceNotFoundException && config.ignore_unknown) return;
                        api.getLogger().warning("獲取插件 " + plugin + " 的最新版本時出現錯誤: " + err.getMessage());
                        if (!(err instanceof IOException)) err.printStackTrace();
                    });
                }
            }
        }
    }

    private static final Pattern pt = Pattern.compile("(^[\\d\\.]+)");

    public static boolean versionNewer(String versionCurrent, String versionLatest) {
        var unequal = DragoniteMC.getDnmCoreConfig().getVersionChecker().use_unequal_check;
        if (unequal) return versionCurrent.equals(versionLatest);
        else {
            if (versionCurrent.equals(versionLatest)) return true;
            var currentMatcher = pt.matcher(versionCurrent);
            var latestMatcher = pt.matcher(versionLatest);
            String[] current;
            String[] latest;
            if (currentMatcher.find()){
                current = currentMatcher.group().split("\\.");
            }else{
                return false;
            }
            if (latestMatcher.find()){
                latest = latestMatcher.group().split("\\.");
            }else{
                return true;
            }
            int length = Math.max(current.length, latest.length);
            for (int i = 0; i < length; i++) {
                int currentNum = i < current.length ? Integer.parseInt(current[i]) : 0;
                int latestNum = i < latest.length ? Integer.parseInt(latest[i]) : 0;
                if (currentNum > latestNum) return true;
                else if (currentNum < latestNum) return false;
            }
            return true;
        }
    }
}
