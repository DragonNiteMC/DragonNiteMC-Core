package com.dragonite.mc.dnmc.core.command.dnmc.world.setter;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;

public class WorldSetMainCommand extends DefaultCommand {

    public WorldSetMainCommand(CommandNode parent) {
        super(parent, "set", "dragonite.world.set", "世界設置指令");
        this.addSub(new WorldSetPvECommand(this));
        this.addSub(new WorldSetPvPCommand(this));
        this.addSub(new WorldSetVulnerCommand(this));
    }
}
