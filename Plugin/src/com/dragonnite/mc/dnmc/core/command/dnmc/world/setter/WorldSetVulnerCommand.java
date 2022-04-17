package com.dragonnite.mc.dnmc.core.command.dnmc.world.setter;

import com.dragonnite.mc.dnmc.core.command.dnmc.world.WorldCommandNode;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldSetVulnerCommand extends WorldCommandNode {

    public WorldSetVulnerCommand(CommandNode parent) {
        super(parent, "vulnerable", "dragonnite.world.set.vulnerable", "設置是否無敵", "<world> <invincible>");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        String name = args.get(0);
        if (!args.get(1).equalsIgnoreCase("true") && !args.get(1).equals("false")) {
            sender.sendMessage(DragonNiteMC.getAPI().getCoreConfig().getPrefix() + "§c不是布爾值！");
            return true;
        }

        boolean vulnerable = Boolean.parseBoolean(args.get(1));

        try {
            DragonNiteMC.getDnmcWorldManager().updateWorldProperties(name, p -> p.setVulnerable(vulnerable));
            sender.sendMessage(prefix + "§a設置成功。");
        } catch (WorldNonExistException e) {
            sender.sendMessage(prefix + "§c該世界尚未加載或不存在。");
        }
        return true;
    }
}
