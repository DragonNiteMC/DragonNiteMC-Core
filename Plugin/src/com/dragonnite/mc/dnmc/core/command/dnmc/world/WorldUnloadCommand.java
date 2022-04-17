package com.dragonnite.mc.dnmc.core.command.dnmc.world;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldUnloadCommand extends WorldCommandNode {
    public WorldUnloadCommand(CommandNode parent) {
        super(parent, "unload", "dragonnite.world.unload", "卸載世界", "<world>");
    }

    public static boolean validateDefaultWorld(String name) {
        return Bukkit.getServer().getWorlds().get(0).getName().equals(name);
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        String name = args.get(0);
        if (validateDefaultWorld(name)) {
            sender.sendMessage(prefix + "§c你無法卸載默認世界。");
            return false;
        }
        try {
            boolean success = DragonNiteMC.getDnmcWorldManager().unloadWorld(name);
            sender.sendMessage(prefix + "世界 " + name + " 卸載 " + (success ? "成功" : "失敗") + "。");

        } catch (WorldNonExistException e) {
            sender.sendMessage(prefix + "§c世界 " + e.getWorld() + " 不存在!");
        }
        return true;
    }
}
