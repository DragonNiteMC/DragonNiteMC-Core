package com.dragonite.mc.dnmc.core.worlds;

import com.dragonite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonite.mc.dnmc.core.misc.world.WorldProperties;
import com.google.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class WorldPropertiesManager {

    private final Map<String, WorldProperties> propertiesMap = new ConcurrentHashMap<>();
    private final File folder;
    private final Plugin plugin;

    @Inject
    WorldPropertiesManager(Plugin plugin) {
        this.plugin = plugin;
        this.folder = new File(plugin.getDataFolder(), "WorldData");
        if (!folder.exists()) folder.mkdirs();

    }

    private FileConfiguration injectProperties(WorldProperties properties) {
        FileConfiguration yml = new YamlConfiguration();
        yml.set("pvp", properties.isPvp());
        yml.set("auto-load", properties.isAutoLoad());
        yml.set("pve", properties.isPve());
        yml.set("vulnerable", properties.isVulnerable());
        yml.createSection("spawn", properties.getSpawn().serialize());
        return yml;
    }

    boolean createProperties(World world) {
        propertiesMap.put(world.getName(), new WorldProperties(world.getPVP(), true, world.getSpawnLocation(), true, true));
        WorldProperties properties = propertiesMap.get(world.getName());
        var data = injectProperties(properties);
        return save(world.getName(), data, 0);
    }

    boolean deleteProperties(String world) throws WorldNonExistException {
        if (delete(world, 0)) {
            return this.unloadProperties(world) != null;
        } else {
            return false;
        }
    }

    @Nonnull
    WorldProperties getProperties(String world) throws WorldNonExistException {
        return Optional.ofNullable(propertiesMap.get(world)).orElseThrow(() -> new WorldNonExistException(world));
    }

    boolean updateProperties(String world, WorldProperties properties) {
        FileConfiguration config = injectProperties(properties);
        return save(world, config, 0);
    }

    boolean updateProperties(String world, Consumer<WorldProperties> editor) throws WorldNonExistException {
        var properties = Optional.ofNullable(propertiesMap.get(world)).orElseThrow(() -> new WorldNonExistException(world));
        editor.accept(properties);
        return this.updateProperties(world, properties);
    }

    @Nonnull
    WorldProperties loadProperties(String world) throws WorldNonExistException {
        WorldProperties properties = fromYamlStorage(world);
        this.propertiesMap.put(world, properties);
        return properties;
    }

    @Nonnull
    private WorldProperties fromYamlStorage(String world) throws WorldNonExistException {
        var file = new File(folder, world + ".yml");
        if (!file.exists()) throw new WorldNonExistException(world);
        FileConfiguration yml = YamlConfiguration.loadConfiguration(file);
        boolean pvp = yml.getBoolean("pvp");
        boolean autoLoad = yml.getBoolean("auto-load");
        boolean pve = yml.getBoolean("pve");
        boolean vulnerable = yml.getBoolean("vulnerable");
        ConfigurationSection spawnSection = yml.getConfigurationSection("spawn");
        if (spawnSection != null){
            double x = spawnSection.getDouble("x");
            double y = spawnSection.getDouble("y");
            double z = spawnSection.getDouble("z");
            String worldName = Optional.ofNullable(spawnSection.getString("world")).orElse("");
            float pitch = (float) spawnSection.getDouble("pitch");
            float yaw = (float) spawnSection.getDouble("yaw");
            var bw = Bukkit.getWorld(worldName);
            if (bw != null) {
                Location spawn = new Location(bw, x, y, z, pitch, yaw);
                return new WorldProperties(pvp, pve, spawn, vulnerable, autoLoad);
            }
        }
        return new WorldProperties(pvp, pve, null, vulnerable, autoLoad);
    }

    @Nonnull
    WorldProperties getPropertiesExact(String world) throws WorldNonExistException {
        var properties = this.propertiesMap.get(world);
        if (properties == null) {
            return fromYamlStorage(world);
        } else {
            return properties;
        }
    }

    boolean hasProperties(String world){
        return this.propertiesMap.containsKey(world);
    }

    @Nullable
    WorldProperties unloadProperties(String world) {
        return this.unloadProperties(world, true);
    }

    @Nullable
    WorldProperties unloadProperties(String world, boolean save) {
        var properties = this.propertiesMap.remove(world);
        if (properties != null && save) {
            this.updateProperties(world, properties);
        }
        return properties;
    }

    void saveProperties() {
        this.propertiesMap.forEach(this::updateProperties);
    }


    private boolean save(String world, FileConfiguration configuration, int times) {
        try {
            configuration.save(new File(folder, world + ".yml"));
            return true;
        } catch (IOException e) {
            if (times >= 5) {
                plugin.getLogger().warning("Cannot save world file " + world);
                e.printStackTrace();
            } else {
                plugin.getLogger().warning("saving world properties " + world + " failed, retrying " + times + " times");
                return this.save(world, configuration, ++times);
            }
        }
        return false;
    }

    private boolean delete(String world, int times) throws WorldNonExistException {
        var file = new File(folder, world + ".yml");
        if (!file.exists()) throw new WorldNonExistException(world);
        try {
            FileUtils.forceDelete(file);
            return true;
        } catch (IOException e) {
            if (times >= 5) {
                plugin.getLogger().warning("Cannot delete world properties " + world);
                e.printStackTrace();
            } else {
                plugin.getLogger().warning("deleting world properties " + world + " failed, retrying " + times + " times");
                this.delete(world, ++times);
            }

        }
        return false;
    }

}
