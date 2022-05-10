package com.dragonite.mc.dnmc.core.worlds;

import com.dragonite.mc.dnmc.core.managers.WorldManager;
import com.dragonite.mc.dnmc.core.misc.world.WorldExistException;
import com.dragonite.mc.dnmc.core.misc.world.WorldLoadedException;
import com.dragonite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonite.mc.dnmc.core.misc.world.WorldProperties;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public final class BukkitWorldHandler implements WorldManager {

    private final Random random = new Random();
    private final Plugin plugin;
    private final ChunkGenerator voidChunkGenerator;

    @Inject
    private WorldPropertiesManager propertiesManager;

    @Inject
    public BukkitWorldHandler(Plugin plugin) {
        this.plugin = plugin;
        this.voidChunkGenerator = new VoidChunkGenerator();
    }

    public void loadDefaultWorld(){
        var defaultWorld = Bukkit.getWorlds().get(0);
        try {
            propertiesManager.loadProperties(defaultWorld.getName());
        } catch (WorldNonExistException e) {
            plugin.getLogger().info("Default World doesn't have WorldProperties, created one.");
            propertiesManager.createProperties(defaultWorld);
        }
    }

    @Override
    public CompletableFuture<Boolean> enableWorld(@Nonnull String world) throws WorldNonExistException, WorldLoadedException {
        var properties = propertiesManager.getPropertiesExact(world);
        if (properties.isAutoLoad()) {
            throw new IllegalStateException("enabling a world which is already enabled.");
        }
        return this.loadWorld(world).thenApply(Objects::nonNull);
    }

    @Override
    public String[] listWorldMessages() {
        var worlds = this.getWorldList().entrySet().stream()
                .map(en ->
                        ChatColor.YELLOW + en.getKey() + "§7 - " + (en.getValue() ? "§a已啟用" : "§c已禁用")
                                + (propertiesManager.hasProperties(en.getKey()) ? "§f(已加載)" : "§7(未加載)"));
        return worlds.map(l -> DragoniteMC.getDnmCoreConfig().getPrefix() + l).toArray(String[]::new);
    }

    @Override
    public Map<String, Boolean> getWorldList() {
        Map<String, Boolean> worlds = new LinkedHashMap<>();
        File[] wFiles = Bukkit.getWorldContainer().listFiles();
        if (wFiles == null) return Map.of();
        for (File wFile : wFiles) {
            String name = FilenameUtils.getBaseName(wFile.getName());
            if (name.isEmpty()) continue;
            try {
                var autoload = propertiesManager.getPropertiesExact(name).isAutoLoad();
                worlds.put(name, autoload);
            } catch (WorldNonExistException ignored) {
            }
        }
        return worlds;
    }

    @Override
    public Optional<ChunkGenerator> getChunkGenerator(@Nonnull String world, String name) {
        if (name.equals("void")){
            return Optional.of(voidChunkGenerator);
        }
        return Optional.ofNullable(WorldCreator.getGeneratorForName(world, name, Bukkit.getConsoleSender()));
    }

    @Override
    public <T> void applyGameRules(Map<GameRule<T>, T> rules, @Nonnull World world) {
        rules.forEach(world::setGameRule);
        world.save();
    }


    @Override
    public boolean disableWorld(@Nonnull String world) throws WorldNonExistException {
        var properties = propertiesManager.getPropertiesExact(world);
        if (!properties.isAutoLoad()) {
            throw new IllegalStateException("disabling a world which is already disabled.");
        }
        properties.setAutoLoad(false);
        return unloadWorld(world);
    }


    @Override
    public CompletableFuture<World> loadWorld(@Nonnull String world) throws WorldNonExistException, WorldLoadedException {
        if (nonExistWorld(world)) throw new WorldNonExistException(world);
        if (Bukkit.getWorld(world) != null) throw new WorldLoadedException(world);
        CompletableFuture<World> future = new CompletableFuture<>();
        final Function<World, Boolean> finalAfterRun = w -> {
            try {
                var properties = propertiesManager.loadProperties(world);
                properties.setAutoLoad(true);
                return propertiesManager.updateProperties(world, properties);
            } catch (WorldNonExistException e) {
                plugin.getLogger().info("WorldProperties of " + world + " does not exist, creating new...");
                return propertiesManager.createProperties(w);
            }
        };
        WorldCreator creator = WorldCreator.name(world);
        new WorldLoadRunnable(creator, w -> {
            if (w != null && finalAfterRun.apply(w)) {
                future.complete(w);
            }else{
                future.complete(null);
            }
        }).runTask(plugin);
        return future;
    }

    @Override
    public void saveAll() {
        propertiesManager.saveProperties();
    }

    public void createProperties(World world) {
        propertiesManager.createProperties(world);
    }


    private World validateWorld(String world) throws WorldNonExistException {
        if (nonExistWorld(world)) throw new WorldNonExistException(world);
        World w = Bukkit.getWorld(world);
        if (w == null) throw new WorldNonExistException(world);
        return w;
    }

    private boolean nonExistWorld(String world) {
        return !new File(Bukkit.getServer().getWorldContainer(), world).exists();
    }


    @Override
    public boolean unloadWorld(@Nonnull String world) throws WorldNonExistException {
        var bukkitWorld = validateWorld(world);
        if (bukkitWorld.getPlayerCount() > 0) return false;
        if (Bukkit.unloadWorld(bukkitWorld, true)) {
            return propertiesManager.unloadProperties(world) != null;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteWorld(String world) throws WorldNonExistException {
        if (nonExistWorld(world)) throw new WorldNonExistException(world);
        if (this.unloadWorld(world) && propertiesManager.deleteProperties(world)) {
            return deleteWorld(world, 0);
        } else {
            return false;
        }
    }

    private boolean deleteWorld(String world, int times) {
        var worldFile = new File(Bukkit.getServer().getWorldContainer(), world);
        try {
            FileUtils.forceDelete(worldFile);
            return true;
        } catch (IOException e) {
            if (times >= 5) {
                plugin.getLogger().warning("Cannot delete world file " + world);
                e.printStackTrace();
                return false;
            } else {
                plugin.getLogger().warning("deleting world file " + world + " failed, retrying " + times + " times");
                return this.deleteWorld(world, ++times);
            }
        }
    }


    @Override
    public WorldProperties getWorldProperties(@Nonnull String name) throws WorldNonExistException {
        return WorldProperties.copyOf(propertiesManager.getProperties(name));
    }

    @Override
    public boolean updateWorldProperties(@Nonnull String name, Consumer<WorldProperties> editor) throws WorldNonExistException {
        return propertiesManager.updateProperties(name, editor);
    }

    @Override
    public CompletableFuture<World> createWorld(@Nonnull String world, WorldType type, World.Environment environment) throws WorldExistException {
        return this.createWorld(world, type, environment, true);
    }

    @Override
    public CompletableFuture<World> createVoidWorld(@Nonnull String world) throws WorldExistException {
        if (Bukkit.getWorld(world) != null || !nonExistWorld(world)) throw new WorldExistException(world);
        CompletableFuture<World> future = new CompletableFuture<>();
        WorldCreator creator = WorldCreator.name(world)
                .generator(voidChunkGenerator)
                .type(WorldType.FLAT)
                .environment(World.Environment.NORMAL)
                .generateStructures(false);
        this.handleWorldCreate(future, creator);
        return future;
    }


    @Override
    public CompletableFuture<World> createWorld(@Nonnull String world, WorldType type, World.Environment environment, boolean generateStructures) throws WorldExistException {
        return this.createWorld(world, environment, null, generateStructures, type, random.nextLong());
    }


    @Override
    public CompletableFuture<World> createWorld(@Nonnull String world, World.Environment environment, ChunkGenerator generator, boolean generateStructures, WorldType type, long seed) throws WorldExistException {
        if (Bukkit.getWorld(world) != null || !nonExistWorld(world)) throw new WorldExistException(world);
        CompletableFuture<World> future = new CompletableFuture<>();
        WorldCreator creator = WorldCreator.name(world)
                .environment(environment)
                .generator(generator)
                .generateStructures(generateStructures)
                .type(type).seed(seed);
        this.handleWorldCreate(future, creator);
        return future;
    }

    private void handleWorldCreate(CompletableFuture<World> future, WorldCreator creator){
        new WorldLoadRunnable(creator, w -> {
            if (w != null && propertiesManager.createProperties(w)) {
                future.complete(w);
            }else{
                future.complete(null);
            }
        }).runTask(plugin);
    }

}
