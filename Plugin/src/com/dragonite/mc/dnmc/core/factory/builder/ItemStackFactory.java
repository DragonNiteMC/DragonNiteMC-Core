package com.dragonite.mc.dnmc.core.factory.builder;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtWrapper;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractItemStackBuilder;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class ItemStackFactory implements AbstractItemStackBuilder {

    private final ItemStack item;
    private String onClickId;
    private String onInteractId;
    private Consumer<InventoryClickEvent> clickAction;
    private Consumer<PlayerInteractEvent> interactAction;
    private UUID skinUniqueId;
    private String skinName;

    public ItemStackFactory() {
        this(Material.STONE);
    }

    public ItemStackFactory(Material m) {
        this(new ItemStack(m));
    }

    public ItemStackFactory(ItemStack item) {
        this.item = item;
    }

    @Override
    public AbstractItemStackBuilder material(Material m) {
        item.setType(m);
        return this;
    }

    @Override
    public AbstractItemStackBuilder durability(int dur) {
        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.setDamage(dur);
            item.setItemMeta(meta);
        }
        return this;
    }

    @Override
    public AbstractItemStackBuilder enchant(Enchantment enchantment, int level) {
        item.addEnchantment(enchantment, level);
        return this;
    }

    @Override
    public AbstractItemStackBuilder enchant(Map<Enchantment, Integer> enchantmentMap) {
        enchantmentMap.forEach(this::enchant);
        return this;
    }

    @Override
    public AbstractItemStackBuilder openGui(Supplier<Inventory> inventorySupplier) {
        return this.onClick(e -> DragoniteMC.getAPI().getCoreScheduler().runTask(() -> e.getWhoClicked().openInventory(inventorySupplier.get())));
    }

    @Override
    public AbstractItemStackBuilder stack(int s) {
        item.setAmount(s);
        return this;
    }

    @Override
    public AbstractItemStackBuilder displayName(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public AbstractItemStackBuilder lore(String... lores) {
        ItemMeta meta = item.getItemMeta();
        List<String> loreList = meta.getLore();
        if (loreList == null) loreList = new ArrayList<>();
        for (String lore : lores) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', lore));
        }
        meta.setLore(loreList);
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public AbstractItemStackBuilder lore(List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        lore = lore.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public AbstractItemStackBuilder unbreakable(boolean unbreakable) {
        var meta = item.getItemMeta();
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public AbstractItemStackBuilder onClick(Consumer<InventoryClickEvent> action) {
        this.onClickId = UUID.randomUUID().toString();
        this.clickAction = action;
        return this;
    }

    @Override
    public AbstractItemStackBuilder onInteract(Consumer<PlayerInteractEvent> action) {
        this.onInteractId = UUID.randomUUID().toString();
        this.interactAction = action;
        return this;
    }

    @Override
    public AbstractItemStackBuilder head(UUID uuid) {
        return this.head(uuid, null);
    }

    @Override
    public AbstractItemStackBuilder head(UUID uuid, String player) {
        if (this.item.getType() != Material.PLAYER_HEAD && this.item.getType() != Material.PLAYER_WALL_HEAD) {
            throw new IllegalStateException("Cannot set the head skin in " + this.item.getType().toString());
        }
        this.skinUniqueId = uuid;
        this.skinName = player;
        return this;
    }

    @Override
    public AbstractItemStackBuilder modelData(int data) {
        var meta = item.getItemMeta();
        meta.setCustomModelData(data);
        item.setItemMeta(meta);
        return this;
    }

    @Override
    public AbstractItemStackBuilder itemFlags(ItemFlag... itemFlags) {
        item.addItemFlags(itemFlags);
        return this;
    }

    @Override
    public void buildWithSkin(Consumer<ItemStack> callback) {
        var item = this.build();
        if (this.skinName != null && this.skinUniqueId != null) {
            DragoniteMC.getAPI().getPlayerSkinManager().setSkullMeta(this.skinUniqueId, this.skinName, item, callback);
        } else if (this.skinUniqueId != null) {
            DragoniteMC.getAPI().getPlayerSkinManager().setSkullMeta(this.skinUniqueId, item, callback);
        } else {
            callback.accept(item);
        }
    }

    @Override
    public ItemStack build() {
        ItemStack itemStack = MinecraftReflection.getBukkitItemStack(item);
        if (item == null || item.getType() == Material.AIR) return itemStack;
        Optional<NbtWrapper<?>> wrapper = NbtFactory.fromItemOptional(itemStack);
        if (wrapper.isEmpty()) return itemStack;
        NbtCompound compound = NbtFactory.asCompound(wrapper.get());
        if (clickAction != null) {
            compound.put("onClick", onClickId);
            DragoniteMC.getItemEventManager().registerItem(onClickId, clickAction);
        }
        if (interactAction != null) {
            compound.put("onInteract", onInteractId);
            DragoniteMC.getItemEventManager().registerItem(onInteractId, interactAction);
        }
        NbtFactory.setItemTag(itemStack, compound);
        return itemStack;
    }
}
