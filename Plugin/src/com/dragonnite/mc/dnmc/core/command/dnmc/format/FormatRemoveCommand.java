package com.dragonnite.mc.dnmc.core.command.dnmc.format;

import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class FormatRemoveCommand extends CommandNode {


    public FormatRemoveCommand(CommandNode parent) {
        super(parent, "remove", "dragonnite.format.remove", "刪除該群組的聊天格式", "<group>");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {

        FormatDatabaseManager format = DragonNiteMC.getFormatDatabaseManager();
        DNMCoreConfig cf = DragonNiteMC.getDnmCoreConfig();
        String group = args.get(0);
        if (!format.getMap().containsKey(group)) {
            sender.sendMessage(cf.getPrefix() + "§c沒有此群組。");
            return true;
        }

        //Success message
        String success = cf.getPrefix() + "§a更改成功。";

        //Fail message
        String fail = cf.getPrefix() + "§c更改失敗。";

        DragonNiteMC.getAPI().getCoreScheduler().runAsync(() -> sender.sendMessage(format.removeChatformat(group) ? success : fail));

        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }
}
