package com.dragonite.mc.dnmc.core.command.dnmc.format;

import com.dragonite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.utils.Tools;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class FormatAddCommand extends CommandNode {


    public FormatAddCommand(CommandNode parent) {
        super(parent, "add", "dragonite.format.add", "添加新chatformat", "<group> <priority> <format>");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {

        DNMCoreConfig cf = DragoniteMC.getDnmCoreConfig();
        FormatDatabaseManager format = DragoniteMC.getFormatDatabaseManager();
        try {
            String group = args.get(0);
            var groups = LuckPermsProvider.get().getGroupManager().getGroup(group);
            if (groups == null) {
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

            DragoniteMC.getAPI().getCoreScheduler().runAsync(() -> {
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
