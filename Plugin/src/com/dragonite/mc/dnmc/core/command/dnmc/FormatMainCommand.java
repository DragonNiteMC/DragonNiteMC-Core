package com.dragonite.mc.dnmc.core.command.dnmc;

import com.dragonite.mc.dnmc.core.command.dnmc.format.*;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;

public class FormatMainCommand extends DefaultCommand {


    public FormatMainCommand(CommandNode parent) {
        super(parent, "format", "dragonite.format", "查看指令幫助");
        this.addSub(new FormatAddCommand(this));
        this.addSub(new FormatCheckCommand(this));
        this.addSub(new FormatEditChatCommand(this));
        this.addSub(new FormatEditLevelCommand(this));
        this.addSub(new FormatRemoveCommand(this));
    }
}
