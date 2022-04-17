package com.dragonnite.mc.dnmc.core.misc.commands;

import com.dragonnite.mc.dnmc.core.exception.NotExecutableException;
import com.dragonnite.mc.dnmc.core.misc.commands.functional.CmdExecution;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 指令節點建造器
 *
 * @author Eric Lam
 * @see CommandNode
 */
public class CommandNodeBuilder {

    private String command;
    private String permission;
    private String description;
    private String placeholder;
    private CommandNode parent;
    private String[] alias = new String[0];
    private CmdExecution<List<String>> tabCompleterSender;
    private CmdExecution<Boolean> cmdExecutorSender;

    /**
     * @param command 指令
     */
    public CommandNodeBuilder(String command) {
        this.command = command;
    }

    /**
     * @param permission 權限
     * @return this
     */
    public CommandNodeBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * @param description 介紹
     * @return this
     */
    public CommandNodeBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param placeholder 用法
     * @return this
     */
    public CommandNodeBuilder placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * @param parent 父類指令
     * @return this
     */
    public CommandNodeBuilder parent(CommandNode parent) {
        this.parent = parent;
        return this;
    }

    /**
     * 若果是主指令，將會自動從 plugin.yml 添加
     *
     * @param alias 縮寫指令
     * @return this
     */
    public CommandNodeBuilder alias(String... alias) {
        this.alias = alias;
        return this;
    }

    /**
     * @param executor tab 執行
     * @return this
     */
    public CommandNodeBuilder tabComplete(CmdExecution<List<String>> executor) {
        this.tabCompleterSender = executor;
        return this;
    }

    /**
     * @param cmdExecutor 指令執行
     * @return this
     */
    public CommandNodeBuilder execute(CmdExecution<Boolean> cmdExecutor) {
        this.cmdExecutorSender = cmdExecutor;
        return this;
    }

    /**
     * @return 指令節點
     * @throws NotExecutableException 指令無法執行
     */
    public CommandNode build() {
        if (cmdExecutorSender == null) throw new NotExecutableException(command);
        return new CommandNode(parent, command, permission, description, placeholder, alias) {
            @Override
            public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
                return cmdExecutorSender.execute(sender, args);
            }

            @Override
            public List<String> executeTabCompletion(@Nonnull CommandSender sender, @Nonnull List<String> args) {
                return tabCompleterSender == null ? null : tabCompleterSender.execute(sender, args);
            }
        };
    }


}
