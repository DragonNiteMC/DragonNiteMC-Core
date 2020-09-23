package com.hypernite.mc.hnmc.core.listener;

import com.hypernite.mc.hnmc.core.main.HyperNiteMC;
import com.hypernite.mc.hnmc.core.misc.world.WorldNonExistException;
import com.hypernite.mc.hnmc.core.misc.world.WorldProperties;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.world.WorldLoadEvent;

public class WorldListeners implements Listener {

    @EventHandler
    public void onPvPAndPve(EntityDamageByEntityEvent e) {
        if (e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;
        WorldProperties properties;
        try {
            properties = HyperNiteMC.getHnmcWorldManager().getWorldProperties(e.getEntity().getWorld().getName());
        } catch (WorldNonExistException ignored) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            if (!properties.isPve()) e.setCancelled(true);
            return;
        }
        LivingEntity entity;
        if (e.getDamager() instanceof Projectile) {
            entity = (LivingEntity) ((Projectile) e.getDamager()).getShooter();
        } else if (e.getDamager() instanceof TNTPrimed) {
            entity = (LivingEntity) ((TNTPrimed) e.getDamager()).getSource();
        } else if (e.getDamager() instanceof ThrownPotion) {
            entity = (LivingEntity) ((ThrownPotion) e.getDamager()).getShooter();
        } else if (e.getDamager() instanceof LivingEntity) {
            entity = (LivingEntity) e.getDamager();
        } else {
            return;
        }

        if (!properties.isPvp() && entity instanceof HumanEntity) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        WorldProperties properties;
        try {
            properties = HyperNiteMC.getHnmcWorldManager().getWorldProperties(e.getEntity().getWorld().getName());
        } catch (WorldNonExistException ignored) {
            return;
        }
        if (!properties.isVulnerable()) e.setCancelled(true);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        String name = e.getWorld().getName();
        if (HyperNiteMC.getHnmcWorldManager().getWorldList().containsKey(name)) return;
        HyperNiteMC.getHnmcWorldManager().createProperties(e.getWorld());
    }
}
