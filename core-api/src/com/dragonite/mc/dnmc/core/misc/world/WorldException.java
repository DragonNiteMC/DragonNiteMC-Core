package com.dragonite.mc.dnmc.core.misc.world;

public class WorldException extends Exception {

    private final String world;

    public WorldException(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }
}
