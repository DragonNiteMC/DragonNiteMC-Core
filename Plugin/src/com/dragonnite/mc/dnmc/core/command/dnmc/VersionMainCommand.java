package com.dragonnite.mc.dnmc.core.command.dnmc;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.commands.DefaultCommand;
import com.dragonnite.mc.dnmc.core.command.dnmc.version.VersionCheckCommand;
import com.dragonnite.mc.dnmc.core.command.dnmc.version.VersionFetchCommand;
import com.dragonnite.mc.dnmc.core.command.dnmc.version.VersionUpdateCommand;
import com.dragonnite.mc.dnmc.core.misc.permission.Perm;

public class VersionMainCommand extends DefaultCommand {

    public VersionMainCommand(CommandNode parent) {
        super(parent, "version", Perm.DEVELOPER, "插件版本指令", "v", "ver");
        this.addSub(new VersionCheckCommand(this));
        this.addSub(new VersionFetchCommand(this));
        this.addSub(new VersionUpdateCommand(this));
    }
}
