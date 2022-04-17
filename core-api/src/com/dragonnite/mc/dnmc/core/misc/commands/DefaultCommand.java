package com.dragonnite.mc.dnmc.core.misc.commands;

import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自動返回其分支指令的幫助訊息
 * <br>
 * 適用於有大量分支指令的指令節點。
 */
public class DefaultCommand extends CommandNode {
    /**
     * @param parent      父類指令
     * @param command     指令
     * @param permission  權限
     * @param description 介紹
     * @param alias       縮寫
     */
    public DefaultCommand(CommandNode parent, String command, String permission, String description, String... alias) {
        super(parent, command, permission, description, null, alias);
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        HelpOutputBuilder builder = new HelpOutputBuilder(this);
        List<CommandNode> nodes = getSubCommands().stream().filter(e -> Perm.hasPermission(sender, e.getPermission())).collect(Collectors.toList());
        if (nodes.size() == 0) {
            sender.sendMessage(DragonNiteMC.getAPI().getCoreConfig().getPrefix() + "§c沒有你可用的指令。");
            return true;
        }
        nodes.forEach(builder::append);
        sender.spigot().sendMessage(builder.build());
        return true;
    }

    @Override
    public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        ArrayList<String> result = new ArrayList<>();
        getSubCommands().forEach(sub -> result.add(sub.getAlias().get(0)));
        return result;
    }
}
