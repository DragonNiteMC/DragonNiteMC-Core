package com.dragonite.mc.dnmc.core.command.dnmc.world;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldListCommand extends WorldCommandNode {

    public WorldListCommand(CommandNode parent) {
        super(parent, "list", "dragonite.world.list", "顯示世界列表", null);
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        String[] msg = DragoniteMC.getDnmcWorldManager().listWorldMessages();
        sender.sendMessage(msg);
        return true;
    }

}
