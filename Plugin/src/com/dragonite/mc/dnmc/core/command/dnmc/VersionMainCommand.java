package com.dragonite.mc.dnmc.core.command.dnmc;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;
import com.dragonite.mc.dnmc.core.misc.permission.Perm;
import com.dragonite.mc.dnmc.core.command.dnmc.version.VersionCheckCommand;
import com.dragonite.mc.dnmc.core.command.dnmc.version.VersionFetchCommand;
import com.dragonite.mc.dnmc.core.command.dnmc.version.VersionUpdateCommand;

public class VersionMainCommand extends DefaultCommand {

    public VersionMainCommand(CommandNode parent) {
        super(parent, "version", Perm.DEVELOPER, "插件版本指令", "v", "ver");
        this.addSub(new VersionCheckCommand(this));
        this.addSub(new VersionFetchCommand(this));
        this.addSub(new VersionUpdateCommand(this));
    }
}
