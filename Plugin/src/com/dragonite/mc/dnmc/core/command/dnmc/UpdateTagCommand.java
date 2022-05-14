package com.dragonite.mc.dnmc.core.command.dnmc;

import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.NameTagManager;
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
        NameTagManager nameTagManager = DragoniteMC.getAPI().getNameTagManager();
        DNMCoreConfig coreConfig = DragoniteMC.getDnmCoreConfig();
        Bukkit.getOnlinePlayers().forEach(nameTagManager::updatePlayer);
        sender.sendMessage(coreConfig.getPrefix() + "§aNameTag 名稱更新成功。");
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        return null;
    }

}
