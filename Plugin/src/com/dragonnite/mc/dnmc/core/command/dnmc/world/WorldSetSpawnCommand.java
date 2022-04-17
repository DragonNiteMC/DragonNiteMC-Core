package com.dragonnite.mc.dnmc.core.command.dnmc.world;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldSetSpawnCommand extends WorldCommandNode {

    public WorldSetSpawnCommand(CommandNode parent) {
        super(parent, "setspawn", "dragonnite.world.setspawn", "設置該世界的重生點", null);

    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(DragonNiteMC.getAPI().getCoreConfig().getPrefix() + DragonNiteMC.getAPI().getCoreConfig().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;
        try {
            DragonNiteMC.getDnmcWorldManager().updateWorldProperties(player.getWorld().getName(), p -> p.setSpawn(player.getLocation()));
            sender.sendMessage(prefix + "§a 世界 " + player.getWorld().getName() + " 的重生點設置成功。");
        } catch (WorldNonExistException e) {
            sender.sendMessage(prefix + "§c世界 " + e.getWorld() + " 不存在(可能未加載)。");
        }
        return true;
    }

}
