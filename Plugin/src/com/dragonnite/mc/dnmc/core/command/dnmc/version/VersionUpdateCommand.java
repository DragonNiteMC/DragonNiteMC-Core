package com.dragonnite.mc.dnmc.core.command.dnmc.version;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.exception.PluginNotFoundException;
import com.dragonnite.mc.dnmc.core.exception.ResourceNotFoundException;
import com.dragonnite.mc.dnmc.core.managers.ResourceManager;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.command.CommandSender;

public class VersionUpdateCommand extends VersionCommandNode {

    public VersionUpdateCommand(CommandNode parent) {
        super(parent, "update", Perm.DEVELOPER, "更新插件到最新版本", "<plugin>", "download");
    }

    @Override
    public void executeChecker(CommandSender sender, ResourceManager manager, String plugin, String version) throws ResourceNotFoundException, PluginNotFoundException {
        sender.sendMessage(config.getPrefix()+"§c暫不支援此功能。");
    }
}
