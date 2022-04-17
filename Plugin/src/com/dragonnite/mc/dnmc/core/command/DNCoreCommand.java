package com.dragonnite.mc.dnmc.core.command;

import com.dragonnite.mc.dnmc.core.command.dnmc.*;
import com.dragonnite.mc.dnmc.core.misc.commands.DefaultCommand;

public class DNCoreCommand extends DefaultCommand {

    public DNCoreCommand() {
        super(null, "dnmc", "dragonnite.use", "§bDragonNiteMC 主指令");
        this.addSub(new UpdateTagCommand(this));
        this.addSub(new UpdateListCommand(this));
        this.addSub(new FormatMainCommand(this));
        this.addSub(new HelpListMainCommand(this));
        this.addSub(new WorldMainCommand(this));
        this.addSub(new VersionMainCommand(this));
    }
}
