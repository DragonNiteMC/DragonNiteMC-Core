package com.dragonnite.mc.dnmc.core.managers;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface PlayerSkinManager {

    /**
     * 生成頭顱
     *
     * @param uuid     玩家UUID
     * @param callback 生成頭顱後的處理
     */
    void generateSkull(UUID uuid, Consumer<ItemStack> callback);

    /**
     * 生成頭顱
     *
     * @param uuid     玩家UUID
     * @param name     玩家名稱
     * @param callback 生成頭顱後的處理
     */
    void generateSkull(UUID uuid, String name, Consumer<ItemStack> callback);

    /**
     * @param b64  base64 Texture value
     * @param head 頭顱
     */
    void setSkullMeta(String b64, ItemStack head);

    /**
     * 設置皮膚
     *
     * @param uuid     玩家UUID
     * @param head     玩家名稱
     * @param callback 設置皮膚後的頭顱
     */
    void setSkullMeta(UUID uuid, ItemStack head, Consumer<ItemStack> callback);

    /**
     * @param uuid     玩家UUID
     * @param name     玩家名稱
     * @param head     頭顱
     * @param callback 設置皮膚後的頭顱
     */
    void setSkullMeta(UUID uuid, String name, ItemStack head, Consumer<ItemStack> callback);

    /**
     * @param uuid  玩家UUID
     * @param block 方塊
     * @param wall  是否貼墻
     * @param face  頭顱方向
     */
    void setHeadBlock(UUID uuid, Block block, boolean wall, BlockFace face);

    /**
     * @param uuid   玩家UUID
     * @param player 玩家名稱
     * @param block  方塊
     * @param wall   是否貼墻
     * @param face   頭顱方向
     */
    void setHeadBlock(UUID uuid, String player, Block block, boolean wall, BlockFace face);

    /**
     * @param b64Value Base64 Texture Value
     * @param block    方塊
     * @param wall     是否貼墻
     * @param face     頭顱方向
     */
    void setHeadBlock(String b64Value, Block block, boolean wall, BlockFace face);

    /**
     * @param uuid   玩家UUID
     * @param player 玩家名稱
     * @param block  方塊
     */
    void updateHeadBlock(UUID uuid, String player, Block block);

    /**
     * @param uuid  玩家UUID
     * @param block 玩家名稱
     */
    void updateHeadBlock(UUID uuid, Block block);

    /**
     * @param newB64Value Base64 Texture Value
     * @param block       方塊
     */
    void updateHeadBlock(String newB64Value, Block block);

    /**
     * @param uuid 玩家UUID
     * @return Base64 Texture Value or IOException
     */
    CompletableFuture<String> getTextureValue(UUID uuid);

}
