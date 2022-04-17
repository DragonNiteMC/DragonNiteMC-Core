package com.dragonnite.mc.dnmc.core.command.dnmc.helplist;

import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.config.implement.yaml.HelpConfig;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class HelpListUploadCommand extends CommandNode {

    public HelpListUploadCommand(CommandNode parent) {
        super(parent, "upload", "dragonnite.helplist.upload", "上傳指定的頁面到資料庫(必須跟隨 Help.yml 的路徑)", "<page> <是否為Staff頁面>");
    }

    static List<String> getPages(String[] args, DNMCoreConfig cf, CommandSender sender) {
        switch (args[1]) {
            case "true":
            case "false":
                break;
            default:
                sender.sendMessage(cf.getPrefix() + "§a無效的布爾值。");
                sender.sendMessage(cf.getPrefix() + "§e請輸入 true / false");
                return null;
        }
        String page = args[0].toLowerCase();

        HelpConfig help = cf.getHelp();
        if (help.getList(page).isEmpty()) {
            sender.sendMessage(cf.getPrefix() + "§c沒有這個頁面!");
            return null;
        }

        if (help.getList(page).size() <= 0) {
            sender.sendMessage(cf.getPrefix() + "§c此頁面內容是空的! 請檢查你的 Help.yml !");
            return null;
        }
        return help.getList(page).stream().map(helpString -> cf.getPrefix() + ChatColor.translateAlternateColorCodes('&', helpString)).collect(Collectors.toList());
    }


    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        DNMCoreConfig config = DragonNiteMC.getDnmCoreConfig();
        List<String> list = getPages(args.toArray(String[]::new), config, sender);
        if (list == null) return false;
        boolean isStaff = Boolean.parseBoolean(args.get(1));
        String page = args.get(0).toLowerCase();
        //Success message
        String success = config.getPrefix() + "§a更改成功。";

        //Fail message
        String fail = config.getPrefix() + "§c更改失敗。";

        DragonNiteMC.getAPI().getCoreScheduler().runAsync(() -> {
            boolean done = DragonNiteMC.getHelpPagesManager().uploadPages(page, list, isStaff);
            sender.sendMessage(done ? success : fail);
        });
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }
}
