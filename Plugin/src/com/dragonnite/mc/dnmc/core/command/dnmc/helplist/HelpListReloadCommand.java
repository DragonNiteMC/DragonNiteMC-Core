package com.dragonnite.mc.dnmc.core.command.dnmc.helplist;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class HelpListReloadCommand extends CommandNode {

    public HelpListReloadCommand(CommandNode parent) {
        super(parent, "reload", "dragonnite.helplist.reload", "重新載入Help.yml", null);
    }


    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        DragonNiteMC.getDnmCoreConfig().reloadHelp();
        sender.sendMessage(DragonNiteMC.getDnmCoreConfig().getPrefix() + "§a重載完成。");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }

}
