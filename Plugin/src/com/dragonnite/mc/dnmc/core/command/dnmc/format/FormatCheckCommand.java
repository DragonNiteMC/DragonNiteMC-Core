package com.dragonnite.mc.dnmc.core.command.dnmc.format;

import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.chatformat.ChatFormat;
import com.dragonnite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class FormatCheckCommand extends CommandNode {


    public FormatCheckCommand(CommandNode parent) {
        super(parent, "check", "dragonnite.format.check", "查看該群組的format", "<group>");
    }


    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        FormatDatabaseManager format = DragonNiteMC.getFormatDatabaseManager();
        DNMCoreConfig cf = DragonNiteMC.getDnmCoreConfig();
        if (!format.getMap().containsKey(args.get(0))) {
            sender.sendMessage(cf.getPrefix() + "§c沒有此群組。");
            return true;
        }

        ChatFormat mat = format.getMap().get(args.get(0));

        String[] list = {
                cf.getPrefix() + args.get(0) + " §e的聊天格式:§r " + mat.getChatformat(),
                cf.getPrefix() + args.get(0) + " §a的優先度:§r " + mat.getPriority()
        };

        sender.sendMessage(list);
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }


}
