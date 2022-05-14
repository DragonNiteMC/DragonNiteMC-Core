package com.dragonite.mc.dnmc.core.command.dnmc.helplist;

import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.HelpPagesManager;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class HelpListRemoveCommand extends CommandNode {


    public HelpListRemoveCommand(CommandNode parent) {
        super(parent, "remove", "dragonite.helplist.remove", "刪除指定的頁面或其特定行數", "<page> [line]");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        HelpPagesManager helpPagesManager = DragoniteMC.getHelpPagesManager();
        DNMCoreConfig config = DragoniteMC.getDnmCoreConfig();
        String page = args.get(0).toLowerCase();

        DragoniteMC.getAPI().getCoreScheduler().runAsync(() -> {
            boolean done;
            if (args.size() < 2) {
                done = helpPagesManager.removePage(page);
            } else {
                try {
                    int line = Integer.parseInt(args.get(1));
                    done = helpPagesManager.removePage(page, line);
                } catch (NumberFormatException e) {
                    sender.sendMessage(config.getPrefix() + "§c無效數值。");
                    return;
                }
            }
            //Success message
            String success = config.getPrefix() + "§a更改成功。";

            //Fail message
            String fail = config.getPrefix() + "§c更改失敗。";

            sender.sendMessage(done ? success : fail);

        });

        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }
}
