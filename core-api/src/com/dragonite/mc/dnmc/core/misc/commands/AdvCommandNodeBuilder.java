package com.dragonite.mc.dnmc.core.misc.commands;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.CoreConfig;
import com.dragonite.mc.dnmc.core.exception.NotExecutableException;
import com.dragonite.mc.dnmc.core.misc.commands.functional.AdvCmdExecution;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 指令節點建造器
 *
 * @param <Sender> 發送者指定類別
 * @author Eric Lam
 * @see CommandNode
 */
public class AdvCommandNodeBuilder<Sender extends CommandSender> {

    private final String command;
    private String permission;
    private String description;
    private String placeholder;
    private CommandNode parent;
    private String[] alias = new String[0];
    private AdvCmdExecution<List<String>, Sender> tabCompleterSender;
    private AdvCmdExecution<Boolean, Sender> cmdExecutorSender;

    /**
     * @param command 指令
     */
    public AdvCommandNodeBuilder(String command) {
        this.command = command;

    }

    /**
     * @param permission 權限
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> permission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * @param description 介紹
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param placeholder 用法
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * @param parent 父類指令
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> parent(CommandNode parent) {
        this.parent = parent;
        return this;
    }

    /**
     * 若果是主指令，將會自動從 plugin.yml 添加
     *
     * @param alias 縮寫指令
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> alias(String... alias) {
        this.alias = alias;
        return this;
    }

    /**
     * @param executor tab 執行
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> tabComplete(AdvCmdExecution<List<String>, Sender> executor) {
        this.tabCompleterSender = executor;
        return this;
    }

    /**
     * @param cmdExecutor 指令執行
     * @return this
     */
    public AdvCommandNodeBuilder<Sender> execute(AdvCmdExecution<Boolean, Sender> cmdExecutor) {
        this.cmdExecutorSender = cmdExecutor;
        return this;
    }

    /**
     * @return 指令節點
     * @throws NotExecutableException 指令無法執行
     */
    @SuppressWarnings("unchecked")
    public CommandNode build() {
        CoreConfig config = DragoniteMC.getAPI().getCoreConfig();
        if (cmdExecutorSender == null) throw new NotExecutableException(command);
        return new CommandNode(parent, command, permission, description, placeholder, alias) {
            @Override
            public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
                try {
                    return cmdExecutorSender.execute((Sender) sender, args);
                } catch (ClassCastException e) {
                    sender.sendMessage(config.getPrefix() + ChatColor.RED + "無法使用此指令。");
                }
                return false;
            }

            @Override
            public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
                try {
                    return tabCompleterSender == null ? null : tabCompleterSender.execute((Sender) sender, args);
                } catch (ClassCastException e) {
                    sender.sendMessage(config.getPrefix() + ChatColor.RED + "無法使用此指令。");
                }
                return null;
            }
        };
    }


}



