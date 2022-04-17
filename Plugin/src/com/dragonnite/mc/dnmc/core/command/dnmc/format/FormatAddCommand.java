package com.dragonnite.mc.dnmc.core.command.dnmc.format;

import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.utils.Tools;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class FormatAddCommand extends CommandNode {


    public FormatAddCommand(CommandNode parent) {
        super(parent, "add", "dragonnite.format.add", "添加新chatformat", "<group> <priority> <format>");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {

        DNMCoreConfig cf = DragonNiteMC.getDnmCoreConfig();
        FormatDatabaseManager format = DragonNiteMC.getFormatDatabaseManager();
        try {
            String group = args.get(0);
            var groups = DragonNiteMC.getAPI().getVaultAPI().getPermission().getGroups();
            if (Arrays.binarySearch(groups, group) < 0) {
                sender.sendMessage(cf.getPrefix() + "§c在你的權限插件中沒有此群組。");
                return true;
            }
            int priority = Integer.parseInt(args.get(1));
            String pattern = Tools.toLongString(args.toArray(String[]::new), 2).stripLeading();
            if (format.getMap().containsKey(group)) {
                sender.sendMessage(cf.getPrefix() + "§c此群組名稱已存在!");
                return true;
            }

            //Success message
            String success = cf.getPrefix() + "§a更改成功。";

            //Fail message
            String fail = cf.getPrefix() + "§c更改失敗。";

            DragonNiteMC.getAPI().getCoreScheduler().runAsync(() -> {
                boolean done = format.saveChatformat(group, pattern, priority);
                sender.sendMessage(done ? success : fail);
            });

        } catch (NumberFormatException e) {
            sender.sendMessage(cf.getPrefix() + "§c無效數值。");
        }
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }

}
