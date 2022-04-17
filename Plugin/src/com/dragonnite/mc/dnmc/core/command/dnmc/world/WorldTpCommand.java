package com.dragonnite.mc.dnmc.core.command.dnmc.world;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldTpCommand extends WorldCommandNode {

    public WorldTpCommand(CommandNode parent) {
        super(parent, "teleport", "dragonnite.world.teleport", "傳送到該世界", "<world>", "tp", "goto");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + DragonNiteMC.getAPI().getCoreConfig().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;
        String name = args.get(0);
        if (player.getWorld().getName().equals(name)) {
            sender.sendMessage(prefix + "§c你目前已在該世界。");
        }
        try {
            Location spawn = DragonNiteMC.getDnmcWorldManager().getWorldProperties(name).getSpawn();
            if (spawn == null) {
                sender.sendMessage(prefix + "§c找不到該世界的重生坐標 (未設置?)");
                return true;
            }
            player.teleportAsync(spawn);
        } catch (WorldNonExistException e) {
            sender.sendMessage(prefix + "§c找不到此世界。");
        }
        return true;
    }
}
