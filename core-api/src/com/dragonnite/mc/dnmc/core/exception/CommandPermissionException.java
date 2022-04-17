package com.dragonnite.mc.dnmc.core.exception;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;

/**
 * @see CommandNode
 */
public class CommandPermissionException extends Exception {
    public CommandPermissionException(String msg) {
        super(msg);
    }
}
