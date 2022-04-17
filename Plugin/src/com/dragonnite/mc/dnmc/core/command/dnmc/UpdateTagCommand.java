package com.dragonnite.mc.dnmc.core.command.dnmc;

import com.dragonnite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.NameTagManager;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class UpdateTagCommand extends CommandNode {

    public UpdateTagCommand(CommandNode parent) {
        super(parent, "update-tag", Perm.ADMIN, "更新所有玩家的NameTag名稱", null);
    }


    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        NameTagManager nameTagManager = DragonNiteMC.getAPI().getNameTagManager();
        DNMCoreConfig coreConfig = DragonNiteMC.getDnmCoreConfig();
        Bukkit.getOnlinePlayers().forEach(nameTagManager::updatePlayer);
        sender.sendMessage(coreConfig.getPrefix() + "§aNameTag 名稱更新成功。");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }

}
