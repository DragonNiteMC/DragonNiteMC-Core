package com.dragonite.mc.dnmc.core.command.dnmc;

import com.dragonite.mc.dnmc.core.command.dnmc.helplist.*;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;

public class HelpListMainCommand extends DefaultCommand {


    public HelpListMainCommand(CommandNode parent) {
        super(parent, "helplist", "dragonite.helplist", "查看指令幫助");
        this.addSub(new HelpListEditStaffCommand(this));
        this.addSub(new HelpListReloadCommand(this));
        this.addSub(new HelpListRemoveCommand(this));
        this.addSub(new HelpListReUploadCommand(this));
        this.addSub(new HelpListUploadCommand(this));
    }

}
