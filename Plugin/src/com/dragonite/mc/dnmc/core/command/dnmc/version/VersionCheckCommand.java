package com.dragonite.mc.dnmc.core.command.dnmc.version;

import com.dragonite.mc.dnmc.core.exception.PluginNotFoundException;
import com.dragonite.mc.dnmc.core.exception.ResourceNotFoundException;
import com.dragonite.mc.dnmc.core.managers.ResourceManager;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public class VersionCheckCommand extends VersionCommandNode {


    public VersionCheckCommand(CommandNode parent) {
        super(parent, "check", Perm.DEVELOPER, "檢查該插件的版本是否最新", "<plugin>", "latest");
    }

    @Override
    public void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) throws ResourceNotFoundException, PluginNotFoundException {
        var v = manager.getLatestVersion(plugin);
        var b = manager.isLatestVersion(plugin);
        sender.sendMessage(config.getPrefix()+ (b ? ChatColor.GREEN : ChatColor.RED)+"插件 "+plugin+" 目前版本 v"+version+", 最新版本為 v"+v+", "+(b ? "沒有" : "有")+"可用的更新。");
    }
}
