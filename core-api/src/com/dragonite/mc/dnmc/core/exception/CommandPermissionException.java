package com.dragonite.mc.dnmc.core.exception;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;

/**
 * @see CommandNode
 */
public class CommandPermissionException extends Exception {
    public CommandPermissionException(String msg) {
        super(msg);
    }
}
