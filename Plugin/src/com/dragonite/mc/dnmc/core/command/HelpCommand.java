package com.dragonite.mc.dnmc.core.command;

import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.HelpPages;
import com.dragonite.mc.dnmc.core.managers.HelpPagesManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class HelpCommand implements CommandExecutor {


    @Inject
    private HelpPagesManager helpPagesManager;

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, String[] strings) {
        DNMCoreConfig cm = DragoniteMC.getDnmCoreConfig();
        boolean papiEnabled = cm.isPapiEnabled();
        HashMap<String, HelpPages> pages = helpPagesManager.getMap();
        String page;

        if (strings.length <= 0) page = command.getName().toLowerCase();
        else page = strings[0].toLowerCase();

        if (!pages.containsKey(page)) {
            sender.sendMessage(cm.getPrefix() + "§c未知的幫助指令。");
            return false;
        }

        if (pages.get(page).isStaffPage() && !sender.hasPermission("group.helper")) {
            sender.sendMessage(cm.getPrefix() + cm.getNoPerm());
            return false;
        }

        String[] result;
        if (papiEnabled && sender instanceof Player) {
            Player player = (Player) sender;
            result = pages.get(page).getList().stream().map(text -> PlaceholderAPI.setPlaceholders(player, text)).toArray(String[]::new);
        } else {
            result = pages.get(page).getList().toArray(new String[0]);
        }
        sender.sendMessage(result);
        return true;
    }
}
