package com.dragonite.mc.dnmc.core.command.dnmc;

import com.dragonite.mc.dnmc.core.managers.CoreConfig;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.ChatFormatManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class UpdateListCommand extends CommandNode {

    public UpdateListCommand(CommandNode parent) {
        super(parent, "update-list", Perm.ADMIN, "更新所有玩家的TabList名稱", null);
    }


    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        ChatFormatManager format = DragoniteMC.getAPI().getChatFormatManager();
        CoreConfig coreConfig = DragoniteMC.getDnmCoreConfig();
        Bukkit.getOnlinePlayers().forEach(format::updatePlayerList);
        sender.sendMessage(coreConfig.getPrefix() + "§aTabList 名稱更新成功。");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }
}
