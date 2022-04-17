package com.dragonnite.mc.dnmc.core.updater;

import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.config.implement.yaml.VersionCheckerConfig;
import com.dragonnite.mc.dnmc.core.listener.VersionUpdateListener;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.dragonnite.mc.dnmc.core.exception.PluginNotFoundException;
import com.dragonnite.mc.dnmc.core.exception.ResourceNotFoundException;
import com.dragonnite.mc.dnmc.core.managers.CoreConfig;
import com.dragonnite.mc.dnmc.core.managers.ResourceManager;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SpigotResourceManager implements ResourceManager {

    private static final String WEB_API = "https://api.spiget.org/v2/resources/";
    private static final Gson GSON = new Gson();

    private final Map<String, Version> versionMap = new ConcurrentHashMap<>();

    @Inject
    private CoreConfig coreConfig;

    @Inject
    private Plugin javaPlugin;

    private VersionCheckerConfig getConfig() {
        return ((DNMCoreConfig) coreConfig).getVersionChecker();
    }

    @Override
    public String getLatestVersion(String plugin) throws PluginNotFoundException, ResourceNotFoundException {
        validateResource(plugin);
        return Optional.ofNullable(versionMap.get(plugin)).map(v -> v.name).orElseThrow(() -> new ResourceNotFoundException(plugin));
    }

    @Override
    public boolean isLatestVersion(String plugin) throws PluginNotFoundException, ResourceNotFoundException {
        var resource = validateResource(plugin);
        return VersionUpdateListener.versionNewer(resource.getDescription().getVersion(), getLatestVersion(plugin));
    }

    @Override
    public void fetchLatestVersion(String plugin, Consumer<String> afterRun, Consumer<Exception> errorRun) {
        new SpigotPluginUpdate(plugin, afterRun, errorRun).runTaskAsynchronously(javaPlugin);
    }

    @Override
    public CompletableFuture<File> downloadLatestVersion(String plugin) {
        throw new UnsupportedOperationException("目前暫不支援下載。");
    }

    private Plugin validateResource(String plugin) throws PluginNotFoundException {
        return Optional.ofNullable(javaPlugin.getServer().getPluginManager().getPlugin(plugin)).orElseThrow(() -> new PluginNotFoundException(plugin));
    }

    private static class Version {
        public String name;
        public long id;
    }


    private class SpigotPluginUpdate extends PluginUpdateRunnable {

        protected SpigotPluginUpdate(String plugin, Consumer<String> afterRun, Consumer<Exception> errorRun) {
            super(plugin, afterRun, errorRun);
        }

        @Override
        public String execute(String plugin) throws Exception {
            if (!getConfig().resourceId_to_checks.containsKey(plugin)) {
                throw new PluginNotFoundException(plugin);
            }
            long resourceId = getConfig().resourceId_to_checks.get(plugin);
            URL url = new URL(WEB_API.concat(resourceId + "/versions/latest"));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            if (con.getResponseCode() == 404) {
                throw new ResourceNotFoundException(plugin);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            var version = GSON.fromJson(in, Version.class);
            versionMap.put(plugin, version);
            return version.name;
        }

    }
}
