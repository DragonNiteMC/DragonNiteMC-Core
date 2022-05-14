package com.dragonite.mc.dnmc.core.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Normal Utils
 */
public class Utils {

    public static boolean isWalled(Block block) { //獲取該方塊是否被其中一面包圍
        Location blockLoc = block.getLocation();
        final World world = blockLoc.getWorld();
        final double ox = blockLoc.getX();
        final double oy = blockLoc.getY();
        final double oz = blockLoc.getZ();
        double Bx = ox - 1;
        double Bz = oz - 1;
        double Fx = ox + 1;
        double Fz = oz + 1;
        boolean front = isNotAir(new Location(world, Fx, oy, oz));
        boolean back = isNotAir(new Location(world, Bx, oy, oz));
        boolean right = isNotAir(new Location(world, ox, oy, Fz));
        boolean left = isNotAir(new Location(world, ox, oy, Bz));
        return front || back || right || left;
    }

    public static boolean isFullWalled(Block block) { //獲取該方塊是否被四面包圍
        Location blockLoc = block.getLocation();
        final World world = blockLoc.getWorld();
        final double ox = blockLoc.getX();
        final double oy = blockLoc.getY();
        final double oz = blockLoc.getZ();
        double Bx = ox - 1;
        double Bz = oz - 1;
        double Fx = ox + 1;
        double Fz = oz + 1;
        boolean front = isNotAir(new Location(world, Fx, oy, oz));
        boolean back = isNotAir(new Location(world, Bx, oy, oz));
        boolean right = isNotAir(new Location(world, ox, oy, Fz));
        boolean left = isNotAir(new Location(world, ox, oy, Bz));
        return front && back && right && left;
    }

    private static boolean isNotAir(Location loc) {
        return loc.getBlock().getType() != Material.AIR;
    }

    public static List<Location> circle(Location loc, int radius, int height,
                                        boolean hollow, boolean sphere, int plusY) { //獲取實心/空心的圓柱的坐標
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = (sphere ? cy - radius : cy); y < (sphere ? cy
                        + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z)
                            + (sphere ? (cy - y) * (cy - y) : 0);

                    if (dist < radius * radius
                            && !(hollow && dist < (radius - 1) * (radius - 1))) {
                        Location l = new Location(loc.getWorld(), x, y + plusY,
                                z);
                        circleblocks.add(l);
                    }
                }
            }
        }

        return circleblocks;
    }
}
