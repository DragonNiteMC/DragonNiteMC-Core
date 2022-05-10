package com.dragonnite.mc.dnmc.core.command.dnmc.version;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.exception.PluginNotFoundException;
import com.dragonnite.mc.dnmc.core.exception.ResourceNotFoundException;
import com.dragonnite.mc.dnmc.core.managers.ResourceManager;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.command.CommandSender;

import java.io.IOException;

public class VersionFetchCommand extends VersionCommandNode {

    public VersionFetchCommand(CommandNode parent) {
        super(parent, "fetch", Perm.DEVELOPER, "刷新該插件的最新版本", "<plugin>", "get");
    }

    @Override
    public void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) throws ResourceNotFoundException, PluginNotFoundException {
        sender.sendMessage(config.getPrefix()+"§e正在刷新插件 "+plugin+" 的最新版本...");
        manager.fetchLatestVersion(plugin, v -> {
            sender.sendMessage(config.getPrefix() + "§a 插件 " + plugin + " 的最新版本刷新完畢。 最新版本為 v" + v);
        }, err -> {
            sender.sendMessage(config.getPrefix() + "§c 插件 " + plugin + " 刷新最新版本時出現錯誤: " + err.getMessage());
            if (!(err instanceof IOException)) err.printStackTrace();
        });
    }
}
