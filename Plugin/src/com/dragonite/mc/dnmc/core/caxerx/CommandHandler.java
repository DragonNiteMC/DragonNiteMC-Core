package com.dragonite.mc.dnmc.core.caxerx;

import com.dragonite.mc.dnmc.core.managers.CoreConfig;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.dragonite.mc.dnmc.core.managers.CommandRegister;
import com.dragonite.mc.dnmc.core.exception.CommandArgumentException;
import com.dragonite.mc.dnmc.core.exception.CommandPermissionException;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter, CommandRegister {
    private final List<CommandNode> registeredCommand = new ArrayList<>();

    @Inject
    private CoreConfig coreConfig;

    @Override
    public void registerCommand(@Nonnull JavaPlugin plugin, @Nonnull CommandNode node) {
        PluginCommand plcmd = plugin.getCommand(node.getCommand());
        if (plcmd == null) {
            plugin.getLogger().warning("無法註冊指令 " + node.getCommand() + ", 該指令不在你的 plugin.yml");
            return;
        }
        node.addAllAliases(plcmd.getAliases());
        plcmd.setAliases(node.getAlias());
        registeredCommand.add(node);
        plcmd.setExecutor(this);
        plcmd.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        try {
            for (CommandNode cmd : registeredCommand) {
                if (cmd.match(label)) {
                    return cmd.invokeCommand(sender, Lists.newArrayList(args));
                }
            }
        } catch (CommandPermissionException e) {
            sender.sendMessage(coreConfig.getPrefix() + coreConfig.getNoPerm());
            if (!Perm.hasPermission(sender, Perm.HELPER)) return false;
            sender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + "缺少權限: " + e.getMessage());
        } catch (CommandArgumentException e) {
            sender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + "缺少參數: " + ChatColor.YELLOW + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + "出現錯誤, 請通知本服插件師。");
            sender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + e.toString());
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        try {
            for (CommandNode cmd : registeredCommand) {
                if (cmd.match(label)) {
                    List<String> result = cmd.invokeTabCompletion(sender, Lists.newArrayList(args));
                    String lastAug = args[args.length - 1];
                    if (result != null && !lastAug.equals("")) {
                        result.removeIf(tabItem -> !tabItem.startsWith(lastAug));
                    }
                    return result;
                }
            }
        } catch (CommandPermissionException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + "出現錯誤, 請通知本服插件師。");
            sender.sendMessage(coreConfig.getPrefix() + ChatColor.RED + e.toString());
        }
        return null;
    }
}
