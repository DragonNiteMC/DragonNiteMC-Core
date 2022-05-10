package com.dragonite.mc.dnmc.core.worlds;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public final class WorldLoadRunnable extends BukkitRunnable {

    private final WorldCreator worldCreator;
    private final Consumer<World> asyncRunner;

    public WorldLoadRunnable(WorldCreator worldCreator, Consumer<World> asyncRunner) {
        this.worldCreator = worldCreator;
        this.asyncRunner = asyncRunner;
    }


    @Override
    public void run() {
        World world;
        world = worldCreator.createWorld();
        DragoniteMC.getAPI().getCoreScheduler().runAsync(() -> asyncRunner.accept(world));
    }
}
