package com.dragonnite.mc.dnmc.core.command.dnmc.world.setter;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.commands.DefaultCommand;

public class WorldSetMainCommand extends DefaultCommand {

    public WorldSetMainCommand(CommandNode parent) {
        super(parent, "set", "dragonnite.world.set", "世界設置指令");
        this.addSub(new WorldSetPvECommand(this));
        this.addSub(new WorldSetPvPCommand(this));
        this.addSub(new WorldSetVulnerCommand(this));
    }
}
