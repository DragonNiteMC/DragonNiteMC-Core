package com.dragonite.mc.dnmc.core.misc.world;

import org.bukkit.Location;

public class WorldProperties {

    private boolean pvp;
    private boolean pve;
    private Location spawn;
    private boolean vulnerable;
    private boolean autoLoad;
    private WorldProperties(WorldProperties properties) {
        this(properties.pvp, properties.pve, properties.getSpawn(), properties.vulnerable, properties.autoLoad);
    }
    public WorldProperties(boolean pvp, boolean pve, Location spawn, boolean vulnerable, boolean autoLoad) {
        this.pvp = pvp;
        this.pve = pve;
        this.spawn = spawn;
        this.vulnerable = vulnerable;
        this.autoLoad = autoLoad;
    }

    public WorldProperties(Location spawn) {
        this.spawn = spawn;
    }

    public static WorldProperties copyOf(WorldProperties properties) {
        return new WorldProperties(properties);
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public boolean isPve() {
        return pve;
    }

    public void setPve(boolean pve) {
        this.pve = pve;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public boolean isVulnerable() {
        return vulnerable;
    }

    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }

    public boolean isAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        this.autoLoad = autoLoad;
    }
}
