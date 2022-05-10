package com.dragonite.mc.dnmc.core.builders;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractItemStackBuilder;
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

/**
 * @see AbstractItemStackBuilder
 */
public class ItemStackBuilder implements AbstractItemStackBuilder {

    private final AbstractItemStackBuilder delegate;

    /**
     * @param m 物品類型
     */
    public ItemStackBuilder(Material m) {
        delegate = DragoniteMC.getAPI().getFactory().getBuilder().getItemStackBuilder(m);
    }

    public ItemStackBuilder() {
        delegate = DragoniteMC.getAPI().getFactory().getBuilder().getItemStackBuilder();
    }

    /**
     * @param item 現有物品
     */
    public ItemStackBuilder(ItemStack item) {
        delegate = DragoniteMC.getAPI().getFactory().getBuilder().getItemStackBuilder(item);
    }

    @Override
    public AbstractItemStackBuilder material(Material m) {
        return delegate.material(m);
    }

    @Override
    public AbstractItemStackBuilder durability(int dur) {
        return delegate.durability(dur);
    }

    @Override
    public AbstractItemStackBuilder enchant(Enchantment enchantment, int level) {
        return delegate.enchant(enchantment, level);
    }

    @Override
    public AbstractItemStackBuilder enchant(Map<Enchantment, Integer> enchantmentMap) {
        return delegate.enchant(enchantmentMap);
    }

    @Override
    public AbstractItemStackBuilder openGui(Supplier<Inventory> inventorySupplier) {
        return delegate.openGui(inventorySupplier);
    }

    @Override
    public AbstractItemStackBuilder stack(int s) {
        return delegate.stack(s);
    }

    @Override
    public AbstractItemStackBuilder displayName(String name) {
        return delegate.displayName(name);
    }

    @Override
    public AbstractItemStackBuilder lore(String... lore) {
        return delegate.lore(lore);
    }

    @Override
    public AbstractItemStackBuilder lore(List<String> lore) {
        return delegate.lore(lore);
    }

    @Override
    public AbstractItemStackBuilder unbreakable(boolean unbreakable) {
        return delegate.unbreakable(unbreakable);
    }

    @Override
    public AbstractItemStackBuilder onClick(Consumer<InventoryClickEvent> action) {
        return delegate.onClick(action);
    }

    @Override
    public AbstractItemStackBuilder onInteract(Consumer<PlayerInteractEvent> action) {
        return delegate.onInteract(action);
    }

    @Override
    public AbstractItemStackBuilder head(UUID uuid) {
        return delegate.head(uuid);
    }

    @Override
    public AbstractItemStackBuilder head(UUID uuid, String player) {
        return delegate.head(uuid, player);
    }

    @Override
    public AbstractItemStackBuilder modelData(int data) {
        return delegate.modelData(data);
    }

    @Override
    public AbstractItemStackBuilder itemFlags(ItemFlag... itemFlags) {
        return delegate.itemFlags(itemFlags);
    }

    @Override
    public void buildWithSkin(Consumer<ItemStack> callback) {
        delegate.buildWithSkin(callback);
    }

    @Override
    public ItemStack build() {
        return delegate.build();
    }
}