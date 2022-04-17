package com.dragonnite.mc.dnmc.core.misc.commands.functional;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNodeBuilder;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @see CommandNodeBuilder
 */
@FunctionalInterface
public interface CmdExecution<T> {
    T execute(CommandSender sender, List<String> args);
}
