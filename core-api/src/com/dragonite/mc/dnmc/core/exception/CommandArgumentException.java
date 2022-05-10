package com.dragonite.mc.dnmc.core.exception;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;

/**
 * @see CommandNode
 */
public class CommandArgumentException extends Exception {
    public CommandArgumentException(String msg) {
        super(msg);
    }
}
