package com.dragonnite.mc.dnmc.core.managers.builder;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface AbstractItemStackBuilder extends Buildable<ItemStack> {

    /**
     * @param m 物品類型
     * @return this
     */
    AbstractItemStackBuilder material(Material m);

    /**
     * @param dur 設置耐久度
     * @return this
     */
    AbstractItemStackBuilder durability(int dur);

    /**
     * @param enchantment 附魔
     * @param level       等級
     * @return this
     */
    AbstractItemStackBuilder enchant(Enchantment enchantment, int level);

    /**
     * @param enchantmentMap 附魔 Map
     * @return this
     */
    AbstractItemStackBuilder enchant(Map<Enchantment, Integer> enchantmentMap);

    /**
     * @param inventorySupplier 背包界面
     * @return this
     */
    AbstractItemStackBuilder openGui(Supplier<Inventory> inventorySupplier);

    /**
     * @param s 數量
     * @return this
     */
    AbstractItemStackBuilder stack(int s);

    /**
     * @param name 顯示名稱
     * @return this
     */
    AbstractItemStackBuilder displayName(String name);

    /**
     * @param lore 敘述
     * @return this
     */
    AbstractItemStackBuilder lore(String... lore);

    /**
     * @param lore 敘述
     * @return this
     */
    AbstractItemStackBuilder lore(List<String> lore);

    /**
     * @param unbreakable 設置無法被損壞
     * @return this
     */
    AbstractItemStackBuilder unbreakable(boolean unbreakable);

    /**
     * @param action 點擊事件
     * @return this
     */
    AbstractItemStackBuilder onClick(Consumer<InventoryClickEvent> action);

    /**
     * @param action 物品交互事件
     * @return this
     */
    AbstractItemStackBuilder onInteract(Consumer<PlayerInteractEvent> action);

    /**
     * 設置頭顱皮膚
     *
     * @param uuid 玩家UUID
     * @return this
     */
    AbstractItemStackBuilder head(UUID uuid);

    /**
     * 設置頭顱皮膚
     *
     * @param uuid   玩家 UUID
     * @param player 玩家名稱
     * @return this
     */
    AbstractItemStackBuilder head(UUID uuid, String player);

    /**
     * @param data model 數據
     * @return this
     */
    AbstractItemStackBuilder modelData(int data);

    /**
     * @param itemFlags 物品 Flags
     * @return this
     */
    AbstractItemStackBuilder itemFlags(ItemFlag... itemFlags);

    /**
     * 獲取頭顱皮膚
     *
     * @param callback 設置好皮膚後的頭顱物品
     */
    void buildWithSkin(Consumer<ItemStack> callback);

}
