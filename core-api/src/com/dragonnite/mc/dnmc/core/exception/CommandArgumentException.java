package com.dragonnite.mc.dnmc.core.exception;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;

/**
 * @see CommandNode
 */
public class CommandArgumentException extends Exception {
    public CommandArgumentException(String msg) {
        super(msg);
    }
}
