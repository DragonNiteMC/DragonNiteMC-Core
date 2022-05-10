package com.dragonite.mc.dnmc.core.command.dnmc.world;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldCreateCommand extends WorldCommandNode {

    public WorldCreateCommand(CommandNode parent) {
        super(parent, "create", "dragonite.world.create", "創建世界", "<world> [是否生成建築]", "c");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + DragoniteMC.getAPI().getCoreConfig().getNotPlayer());
            return true;
        }
        Player player = (Player) sender;
        String worldName = args.get(0);
        if (args.size() < 2) {
            new WorldCreationBuilder(worldName, player);
        } else {
            if (!args.get(1).equalsIgnoreCase("true") && !args.get(1).equalsIgnoreCase("false")) {
                sender.sendMessage(prefix + "§c不是布爾值！");
                return true;
            }
            boolean generateStructures = Boolean.parseBoolean(args.get(1));
            new WorldCreationBuilder(worldName, generateStructures, player);
        }
        return true;
    }
}
