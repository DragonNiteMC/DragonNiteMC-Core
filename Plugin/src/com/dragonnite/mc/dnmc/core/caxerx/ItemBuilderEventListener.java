package com.dragonnite.mc.dnmc.core.caxerx;

import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtFactory;
import com.comphenix.protocol.wrappers.nbt.NbtWrapper;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class ItemBuilderEventListener implements Listener {

    private final Map<String, Consumer<? extends Event>> onClickMap = new ConcurrentHashMap<>();
    ;

    public void registerItem(String id, Consumer<? extends Event> action) {
        onClickMap.put(id, action);
    }


    public void removeItem(String id) {
        onClickMap.remove(id);
    }

    @EventHandler
    public void onPlayerClickInventory(InventoryClickEvent e) {
        if (e.getSlotType() == InventoryType.SlotType.OUTSIDE) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        String itemId = getItemId(e.getCurrentItem(), "onClick");
        if (itemId == null) return;
        onClickMap.forEach((id, action) -> {
            if (itemId.equals(id)) {
                if (DragonNiteMC.getDnmCoreConfig().getConfig().clickEventDefaultCancelled) {
                    e.setCancelled(true);
                }
                var eAction = (Consumer<InventoryClickEvent>) action;
                eAction.accept(e);
            }
        });
    }

    @Nullable
    private String getItemId(ItemStack item, String key) {
        item = MinecraftReflection.getBukkitItemStack(item);
        Optional<NbtWrapper<?>> tagOptional;
        try {
            tagOptional = NbtFactory.fromItemOptional(item);
        } catch (IllegalArgumentException ex) {
            DragonNiteMC.plugin.getLogger().warning(ex.getMessage());
            return null;
        }
        if (tagOptional.isEmpty()) return null;
        NbtCompound tag = NbtFactory.asCompound(tagOptional.get());
        if (!tag.containsKey(key)) return null;
        return tag.getString(key);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack mcitem = player.getInventory().getItemInMainHand();
        if (mcitem.getType() == Material.AIR) return;
        String itemId = getItemId(mcitem, "onInteract");
        if (itemId == null) return;
        onClickMap.forEach((id, action) -> {
            if (itemId.equals(id)) {
                if (DragonNiteMC.getDnmCoreConfig().getConfig().interactEventDefaultCancelled) {
                    e.setCancelled(true);
                }
                var eAction = (Consumer<PlayerInteractEvent>) action;
                eAction.accept(e);
            }
        });
    }
}
