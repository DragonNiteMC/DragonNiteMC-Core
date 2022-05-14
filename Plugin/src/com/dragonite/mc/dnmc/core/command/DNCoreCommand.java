package com.dragonite.mc.dnmc.core.command;

import com.dragonite.mc.dnmc.core.command.dnmc.*;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;

public class DNCoreCommand extends DefaultCommand {

    public DNCoreCommand() {
        super(null, "dnmc", "dragonite.use", "§bDragoniteMC 主指令");
        this.addSub(new UpdateTagCommand(this));
        this.addSub(new UpdateListCommand(this));
        this.addSub(new FormatMainCommand(this));
        this.addSub(new HelpListMainCommand(this));
        this.addSub(new WorldMainCommand(this));
        this.addSub(new VersionMainCommand(this));
    }
}
