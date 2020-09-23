package com.hypernite.mc.hnmc.core.listener.cancelevent;

import com.destroystokyo.paper.event.entity.EntityKnockbackByEntityEvent;
import com.hypernite.mc.hnmc.core.config.implement.HNMCoreConfig;
import com.hypernite.mc.hnmc.core.config.implement.yaml.CancelEventConfig;
import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.misc.permission.Perm;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.WeatherEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class OptionalListener implements Listener {

    private final Set<UUID> exception = new HashSet<>();
    private final Map<String, CancelEventConfig.Canceller> section;
    private CancelEventManager cancelEventManager;

    public OptionalListener() {
        HNMCoreConfig cm = HyperNiteMC.getHnmCoreConfig();
        this.section = cm.getCancel().cancelEvents;
        if (section.isEmpty()) {
            HyperNiteMC.plugin.getLogger().info("Cancel Event is empty, skipped.");
            return;
        }
        ClassInfoList events;

        try (ScanResult result = new ClassGraph().whitelistPackages(Event.class.getPackageName()).enableClassInfo().scan()) {
            ClassInfo classInfo = result.getClassInfo(Event.class.getName());
            events = classInfo.getSubclasses().filter(info -> !info.isAbstract());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Set<String> registeredEvents = section.keySet();
        try {
            main:
            for (ClassInfo event : events) {
                //noinspection unchecked
                final Class<? extends Event> eventClass = (Class<? extends Event>) Class.forName(event.getName());
                if (!registeredEvents.contains(eventClass.getSimpleName())) continue;
                Class<?> iterateCls = eventClass;
                while (!containEventHandlers(iterateCls)) {
                    if (iterateCls == iterateCls.getSuperclass()) {
                        continue main;
                    } else {
                        iterateCls = iterateCls.getSuperclass();
                    }
                }

                Bukkit.getPluginManager().registerEvent(eventClass, this, EventPriority.LOWEST, (ig, ev) -> this.onAllEvent(ev), HyperNiteMC.plugin, false);
            }
        } catch (ClassNotFoundException e) {
            HyperNiteMC.plugin.getLogger().warning("Scanned class wasn't found: " + e.getMessage());
        }

        cancelEventManager = (CancelEventManager) HyperNiteMC.getAPI().getEventCancelManager();
        //register world getter
        cancelEventManager.register(WeatherEvent.class, World.class, WeatherEvent::getWorld);
        cancelEventManager.register(WorldEvent.class, World.class, WorldEvent::getWorld);
        cancelEventManager.register(PlayerEvent.class, World.class, playerEvent -> playerEvent.getPlayer().getWorld());
        cancelEventManager.register(EntityEvent.class, World.class, entityEvent -> entityEvent.getEntity().getWorld());
        cancelEventManager.register(BlockEvent.class, World.class, blockEvent -> blockEvent.getBlock().getWorld());
        cancelEventManager.register(HangingEvent.class, World.class, hangingEvent -> hangingEvent.getEntity().getWorld());
        cancelEventManager.register(VehicleEvent.class, World.class, vehicleEvent -> vehicleEvent.getVehicle().getWorld());
        cancelEventManager.register(InventoryEvent.class, World.class, inventoryEvent -> inventoryEvent.getView().getPlayer().getWorld());
        cancelEventManager.register(HangingEvent.class, World.class, hangingEvent -> hangingEvent.getEntity().getWorld());
        cancelEventManager.register(PlayerLeashEntityEvent.class, World.class, e -> e.getEntity().getWorld());

        //register player getter
        cancelEventManager.register(PlayerEvent.class, Player.class, PlayerEvent::getPlayer);
        cancelEventManager.register(EntityKnockbackByEntityEvent.class, Player.class, e -> e.getHitBy() instanceof Player ? (Player) e.getHitBy() : null);
        cancelEventManager.register(EntityDamageByEntityEvent.class, Player.class, e -> e.getDamager() instanceof Player ? (Player) e.getDamager() : null);
        cancelEventManager.register(EntityCombustByEntityEvent.class, Player.class, e -> e.getCombuster() instanceof Player ? (Player) e.getCombuster() : null);
        cancelEventManager.register(EntityEvent.class, Player.class, entityEvent -> entityEvent.getEntity() instanceof Player ? (Player) entityEvent.getEntity() : null);
        cancelEventManager.register(BlockEvent.class, Player.class, blockEvent -> {
            try {
                Method method = blockEvent.getClass().getMethod("getPlayer");
                method.setAccessible(true);
                return (Player) method.invoke(blockEvent);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
                return null;
            }
        });
        cancelEventManager.register(InventoryEvent.class, Player.class, inventoryEvent -> (Player) inventoryEvent.getView().getPlayer());
        cancelEventManager.register(HangingBreakByEntityEvent.class, Player.class, e -> e.getRemover() instanceof Player ? (Player) e.getRemover() : null);
        cancelEventManager.register(VehicleEnterEvent.class, Player.class, e -> e.getEntered() instanceof Player ? (Player) e.getEntered() : null);
        cancelEventManager.register(VehicleExitEvent.class, Player.class, e -> e.getExited() instanceof Player ? (Player) e.getExited() : null);
        cancelEventManager.register(VehicleDestroyEvent.class, Player.class, e -> e.getAttacker() instanceof Player ? (Player) e.getAttacker() : null);
        cancelEventManager.register(VehicleDamageEvent.class, Player.class, e -> e.getAttacker() instanceof Player ? (Player) e.getAttacker() : null);
        cancelEventManager.register(HangingEvent.class, Player.class, e -> e.getEntity() instanceof Player ? (Player) e.getEntity() : null);
        cancelEventManager.register(PlayerLeashEntityEvent.class, Player.class, PlayerLeashEntityEvent::getPlayer);
        cancelEventManager.register(InventoryPickupItemEvent.class, Player.class, e -> e.getInventory().getHolder() instanceof Player ? (Player) e.getInventory().getHolder() : null);

        //register block getter
        cancelEventManager.register(BlockEvent.class, Block.class, BlockEvent::getBlock);
        cancelEventManager.register(PlayerInteractEvent.class, Block.class, PlayerInteractEvent::getClickedBlock);

        //register entity getter
        cancelEventManager.register(EntityEvent.class, Entity.class, EntityEvent::getEntity);
        cancelEventManager.register(HangingEvent.class, Entity.class, HangingEvent::getEntity);
        cancelEventManager.register(PlayerInteractEntityEvent.class, Entity.class, PlayerInteractEntityEvent::getRightClicked);
        cancelEventManager.register(PlayerLeashEntityEvent.class, Entity.class, PlayerLeashEntityEvent::getEntity);

        //register item getter
        cancelEventManager.register(CraftItemEvent.class, ItemStack.class, craftItemEvent -> craftItemEvent.getInventory().getResult());
        cancelEventManager.register(PlayerInteractEvent.class, ItemStack.class, PlayerInteractEvent::getItem);
        cancelEventManager.register(EnchantItemEvent.class, ItemStack.class, EnchantItemEvent::getItem);
        cancelEventManager.register(InventoryMoveItemEvent.class, ItemStack.class, InventoryMoveItemEvent::getItem);
        cancelEventManager.register(InventoryPickupItemEvent.class, ItemStack.class, e -> e.getItem().getItemStack());
    }

    private boolean containEventHandlers(Class<?> eventClass) {
        return Arrays.stream(eventClass.getDeclaredMethods()).anyMatch(method -> method.getParameterCount() == 0 && method.getName().equals("getHandlers"));
    }

    @EventHandler
    public void onCommandPreProcess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (!e.getMessage().equalsIgnoreCase("/exception")) return;
        if (!player.hasPermission(Perm.OWNER)) return;
        e.setCancelled(true);
        if (exception.contains(player.getUniqueId())) {
            exception.remove(player.getUniqueId());
        } else {
            exception.add(player.getUniqueId());
        }
        player.sendMessage(ChatColor.YELLOW + "Exception mode " + (exception.contains(player.getUniqueId()) ? "§aEnabled" : "§cDisabled"));
    }


    private void onAllEvent(Event e) {
        if (!(e instanceof Cancellable)) return;
        Cancellable cancellable = (Cancellable) e;
        Player player = null;
        World world = null;
        String name = e.getEventName();
        if (cancelEventManager.canGetWith(e, World.class)) {
            world = cancelEventManager.getEventWith(e, World.class);
        }
        if (cancelEventManager.canGetWith(e, Player.class)) {
            player = cancelEventManager.getEventWith(e, Player.class);
        }
        if (world == null) return;
        if (player != null && exception.contains(player.getUniqueId())) return;
        CancelEventConfig.Canceller eventSection = section.get(name);
        if (eventSection == null) return;
        boolean whitelist = eventSection.useAsWhitelist;
        List<String> worlds = eventSection.worlds;
        if (worlds.contains(world.getName()) == !whitelist) {
            if (cancelEventManager.canGetWith(e, Block.class)) {
                Block block = cancelEventManager.getEventWith(e, Block.class);
                if (block == null) return;
                CancelEventConfig.Wrapper blocks = eventSection.blocks;
                if (blocks != null) {
                    boolean whitelistBlock = blocks.whitelist;
                    List<String> blockList = Optional.ofNullable(blocks.list).orElse(List.of()).stream().map(String::toUpperCase).collect(Collectors.toList());
                    if (blockList.contains(block.getType().toString()) == !whitelistBlock) {
                        cancellable.setCancelled(true);
                        if (e instanceof PlayerEvent) {
                            ((PlayerEvent) e).getPlayer().sendActionBar("§c本遊戲不允許使用此方塊。");
                        }
                    }
                } else {
                    cancellable.setCancelled(true);
                }
            } else if (cancelEventManager.canGetWith(e, Entity.class)) {
                Entity rc = cancelEventManager.getEventWith(e, Entity.class);
                if (rc == null) return;

                CancelEventConfig.Wrapper entities = eventSection.entities;
                if (entities != null) {
                    boolean whitelistEntity = entities.whitelist;
                    List<String> entityList = Optional.ofNullable(entities.list).orElse(List.of()).stream().map(String::toUpperCase).collect(Collectors.toList());
                    if (entityList.contains(rc.getType().toString()) == !whitelistEntity) {
                        cancellable.setCancelled(true);
                    }
                } else {
                    cancellable.setCancelled(true);
                }

            } else if (cancelEventManager.canGetWith(e, ItemStack.class)) {
                ItemStack resultItem = cancelEventManager.getEventWith(e, ItemStack.class);
                if (resultItem == null) return;
                Material type = resultItem.getType();
                CancelEventConfig.Wrapper items = eventSection.items;
                if (items != null) {
                    boolean whitelistItem = items.whitelist;
                    List<String> entityList = Optional.ofNullable(items.list).orElse(List.of()).stream().map(String::toUpperCase).collect(Collectors.toList());
                    if (entityList.contains(type.toString()) == !whitelistItem) {
                        cancellable.setCancelled(true);
                    }
                } else {
                    cancellable.setCancelled(true);
                }
            } else {
                cancellable.setCancelled(true);
            }
        }
    }


}
