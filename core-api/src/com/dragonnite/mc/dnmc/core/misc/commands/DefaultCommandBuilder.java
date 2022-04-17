package com.dragonnite.mc.dnmc.core.misc.commands;

/**
 * @author Eric Lam
 * @see DefaultCommand
 */
public class DefaultCommandBuilder {
    private String command;
    private String description;
    private String permission;
    private CommandNode parent;
    private CommandNode[] children;
    private String[] alias = new String[0];

    /**
     * @param command 指令
     */
    public DefaultCommandBuilder(String command) {
        this.command = command;
    }

    /**
     * @param description 介紹
     * @return this
     */
    public DefaultCommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param permission 權限
     * @return this
     */
    public DefaultCommandBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * @param parent 父類指令
     * @return this
     */
    public DefaultCommandBuilder parent(CommandNode parent) {
        this.parent = parent;
        return this;
    }

    /**
     * DefaultCommand 將為 它其下的分支指令 製作幫助訊息
     *
     * @param children 其下分支指令
     * @return this
     */
    public DefaultCommandBuilder children(CommandNode... children) {
        this.children = children;
        return this;
    }

    /**
     * 若果是主指令，將會自動從 plugin.yml 添加
     *
     * @param alias 縮寫
     * @return this
     */
    public DefaultCommandBuilder alias(String... alias) {
        this.alias = alias;
        return this;
    }

    /**
     * @return 返回分支指令的幫助訊息的指令
     */
    public DefaultCommand build() {
        DefaultCommand defaultCommand = new DefaultCommand(parent, command, permission, description, alias);
        for (CommandNode node : children) {
            node.setParent(defaultCommand);
            defaultCommand.addSub(node);
        }
        return defaultCommand;
    }
}
