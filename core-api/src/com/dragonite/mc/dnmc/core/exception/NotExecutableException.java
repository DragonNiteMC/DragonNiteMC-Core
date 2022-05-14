package com.dragonite.mc.dnmc.core.exception;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNodeBuilder;
import com.dragonite.mc.dnmc.core.misc.commands.functional.CmdExecution;

/**
 * @see CommandNodeBuilder#execute(CmdExecution)
 */
public class NotExecutableException extends RuntimeException {
    private final String command;

    public NotExecutableException(String command) {
        super("執行函式 為 Null");
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
