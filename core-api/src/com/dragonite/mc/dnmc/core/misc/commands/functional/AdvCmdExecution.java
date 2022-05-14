package com.dragonite.mc.dnmc.core.misc.commands.functional;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNodeBuilder;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @see CommandNodeBuilder
 */
@FunctionalInterface
public interface AdvCmdExecution<T, D extends CommandSender> {
    T execute(D sender, List<String> args);
}
