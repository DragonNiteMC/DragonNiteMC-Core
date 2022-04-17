package com.dragonnite.mc.dnmc.core.misc.commands;


import com.dragonnite.mc.dnmc.core.exception.CommandArgumentException;
import com.dragonnite.mc.dnmc.core.exception.CommandPermissionException;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandNode {

    private final String description;
    private final String placeholder;
    private final String command;


    private final ArrayList<CommandNode> subCommands = new ArrayList<>();


    private final ArrayList<String> alias = new ArrayList<>();


    private final String permission;


    private CommandNode parent;


    /**
     * @param parent      父類指令節點
     * @param command     指令
     * @param permission  權限
     * @param description 介紹
     * @param placeholder 佔位符
     * @param alias       縮寫指令
     */
    public CommandNode(CommandNode parent, @Nonnull String command, String permission, @Nonnull String description, String placeholder, String... alias) {
        this.command = command;
        this.parent = parent;
        this.alias.add(command);
        this.permission = permission;
        this.description = description;
        this.placeholder = placeholder;
        this.alias.addAll(List.of(alias));
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public ArrayList<CommandNode> getSubCommands() {
        return subCommands;
    }

    public ArrayList<String> getAlias() {
        return alias;
    }

    public CommandNode getParent() {
        return parent;
    }

    void setParent(CommandNode node) {
        this.parent = node;
    }

    public String getPermission() {
        return permission;
    }

    public String getCommand() {
        return command;
    }

    @Deprecated
    public void addAlias(@Nonnull String ali) {
        if (!alias.contains(ali)) {
            this.alias.add(ali);
        }
    }

    public void addAllAliases(@Nonnull List<String> aliases) {
        aliases.forEach(ali -> {
            if (!alias.contains(ali)) {
                alias.add(ali);
            }
        });
    }

    public void addSub(@Nonnull CommandNode c) {
        subCommands.add(c);
    }

    /**
     * @param sender 指令發送者
     * @param args   指令參數
     * @return 執行是否成功
     */
    public abstract boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args);

    /**
     * @param sender 指令放著
     * @param args   指令參數
     * @return Tab 列表
     */
    public abstract List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args);

    public boolean invokeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) throws CommandPermissionException, CommandArgumentException {

        if (permission != null && !Perm.hasPermission(sender, permission)) {
            throw new CommandPermissionException(permission);
        }

        if (args.size() > 0) {
            for (CommandNode subCommand : subCommands) {
                if (subCommand.match(args.get(0))) {
                    List<String> passArg = new ArrayList<>(args);
                    passArg.remove(0);
                    if (subCommand.getPlaceholder() != null) {
                        String[] placeholders = Arrays.stream(subCommand.getPlaceholder().split(" ")).filter(holder -> holder.startsWith("<") && holder.endsWith(">")).toArray(String[]::new);
                        if (passArg.size() < placeholders.length) {
                            throw new CommandArgumentException(String.join(" ", placeholders));
                        }
                    }

                    return subCommand.invokeCommand(sender, passArg);
                }
            }
        }

        if (this.getPlaceholder() != null) {
            String[] placeholders = Arrays.stream(this.getPlaceholder().split(" ")).filter(holder -> holder.startsWith("<") && holder.endsWith(">")).toArray(String[]::new);
            if (args.size() < placeholders.length) {
                throw new CommandArgumentException(String.join(" ", placeholders));
            }
        }
        return executeCommand(sender, args);
    }

    public List<String> invokeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) throws CommandPermissionException {
        if (permission != null && !Perm.hasPermission(sender, permission)) {
            throw new CommandPermissionException(permission);
        }

        if (args.size() > 0) {
            for (CommandNode subCommand : subCommands) {
                if (subCommand.match(args.get(0))) {
                    List<String> passArg = new ArrayList<>(args);
                    passArg.remove(0);
                    return subCommand.invokeTabCompletion(sender, passArg);
                }
            }
        }


        return executeTabCompletion(sender, args);
    }

    public boolean match(@Nonnull String args) {
        for (String ali : alias) {
            if (args.equalsIgnoreCase(ali)) {
                return true;
            }
        }
        return false;
    }
}
