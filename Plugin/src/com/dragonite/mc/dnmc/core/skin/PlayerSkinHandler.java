package com.dragonite.mc.dnmc.core.skin;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.dragonite.mc.dnmc.core.managers.CoreScheduler;
import com.dragonite.mc.dnmc.core.managers.PlayerSkinManager;
import com.dragonite.mc.dnmc.core.managers.SkinDatabaseManager;
import com.google.inject.Inject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PlayerSkinHandler implements PlayerSkinManager {

    @Inject
    private SkinDatabaseManager databaseManager;

    @Inject
    private CoreScheduler coreScheduler;

    @Override
    public void generateSkull(UUID uuid, Consumer<ItemStack> callback) {
        coreScheduler.runAsync(() -> {
            var value = databaseManager.getPlayerSkin(uuid);
            coreScheduler.runTask(() -> {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                setSkullMeta(value, head);
                callback.accept(head);
            });
        });
    }

    @Override
    public void generateSkull(UUID uuid, String name, Consumer<ItemStack> callback) {
        coreScheduler.runAsync(() -> {
            var value = databaseManager.getPlayerSkin(uuid, name);
            coreScheduler.runTask(() -> {
                ItemStack head = new ItemStack(Material.PLAYER_HEAD);
                setSkullMeta(value, head);
                callback.accept(head);
            });
        });
    }

    @Override
    public void setSkullMeta(UUID uuid, ItemStack head, Consumer<ItemStack> callback) {
        coreScheduler.runAsync(() -> {
            var value = databaseManager.getPlayerSkin(uuid);
            coreScheduler.runTask(() -> {
                var meta = this.getSkullMeta(value, head);
                head.setItemMeta(meta);
                callback.accept(head);
            });
        });
    }

    @Override
    public void setSkullMeta(UUID uuid, String name, ItemStack head, Consumer<ItemStack> callback) {
        coreScheduler.runAsync(() -> {
            var value = databaseManager.getPlayerSkin(uuid);
            coreScheduler.runTask(() -> {
                var meta = this.getSkullMeta(value, head);
                head.setItemMeta(meta);
                callback.accept(head);
            });
        });
    }


    private SkullMeta getSkullMeta(String b64Value, ItemStack head) {
        GameProfile profile = this.getProfile(b64Value);
        SkullMeta headMeta = (SkullMeta) head.getItemMeta();
        Class<?> headMetaClass = headMeta.getClass();
        try {
            Field profileZlass = headMetaClass.getDeclaredField("profile");
            profileZlass.setAccessible(true);
            profileZlass.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return headMeta;
    }

    @Override
    public void setSkullMeta(String b64, ItemStack head) {
        SkullMeta headMeta = this.getSkullMeta(b64, head);
        head.setItemMeta(headMeta);
    }

    @Override
    public void setHeadBlock(UUID uuid, Block block, boolean wall, BlockFace face) {
        coreScheduler.runAsync(() -> {
            String skin = databaseManager.getPlayerSkin(uuid);
            coreScheduler.runTask(() -> this.setHeadBlock(skin, block, wall, face));
        });
    }

    @Override
    public void setHeadBlock(UUID uuid, String player, Block block, boolean wall, BlockFace face) {
        coreScheduler.runAsync(() -> {
            String skin = databaseManager.getPlayerSkin(uuid, player);
            coreScheduler.runTask(() -> this.setHeadBlock(skin, block, wall, face));
        });
    }

    @Override
    public void setHeadBlock(String b64Value, Block block, boolean wall, BlockFace face) {
        switch (face) {
            case SOUTH:
            case EAST:
            case NORTH:
            case WEST:
                break;
            default:
                face = BlockFace.NORTH;
                break;
        }

        if (wall) block.setType(Material.PLAYER_WALL_HEAD);
        else block.setType(Material.PLAYER_HEAD);
        GameProfile profile = getProfile(b64Value);
        Skull skull = (Skull) block.getState();
        skull.setPlayerProfile(new CraftPlayerProfile(profile));
        BlockData blockData = block.getBlockData();
        if (wall) {
            Directional directional = (Directional) blockData;
            directional.setFacing(face);
            skull.setBlockData(directional);
        } else {
            Rotatable rotatable = (Rotatable) blockData;
            rotatable.setRotation(face.getOppositeFace());
            skull.setBlockData(rotatable);
        }
        skull.update(true);
        block.getState().update(true);
    }

    private GameProfile getProfile(String b64Value) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap != null) {
            propertyMap.put("textures", new Property("textures", b64Value));
        } else {
            throw new IllegalStateException("Profile doesn't contain a property map");
        }
        return profile;
    }

    @Override
    public void updateHeadBlock(UUID uuid, String player, Block block) {
        coreScheduler.runAsync(() -> {
            String skin = databaseManager.getPlayerSkin(uuid, player);
            coreScheduler.runTask(() -> this.updateHeadBlock(skin, block));
        });
    }

    @Override
    public void updateHeadBlock(UUID uuid, Block block) {
        coreScheduler.runAsync(() -> {
            String skin = databaseManager.getPlayerSkin(uuid);
            coreScheduler.runTask(() -> this.updateHeadBlock(skin, block));
        });
    }

    @Override
    public void updateHeadBlock(String newB64Value, Block block) {
        if (block.getType() != Material.PLAYER_HEAD || block.getType() != Material.PLAYER_WALL_HEAD) return;
        GameProfile profile = getProfile(newB64Value);
        Skull skull = (Skull) block.getState();
        skull.setPlayerProfile(new CraftPlayerProfile(profile));
        skull.update(true);
        block.getState().update(true);
    }

    @Override
    public CompletableFuture<String> getTextureValue(UUID uuid) {
        return databaseManager.getTextureValue(uuid);
    }


}
