package com.dragonite.mc.dnmc.core.command.dnmc;

import com.dragonite.mc.dnmc.core.command.dnmc.world.*;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.commands.DefaultCommand;
import com.dragonite.mc.dnmc.core.command.dnmc.world.setter.WorldSetMainCommand;

public class WorldMainCommand extends DefaultCommand {

    public WorldMainCommand(CommandNode parent) {
        super(parent, "world", "dragonite.world", "管理伺服器的世界", "w");
        this.addSub(new WorldCreateCommand(this));
        this.addSub(new WorldCreateVoidCommand(this));
        this.addSub(new WorldDeleteCommand(this));
        this.addSub(new WorldDisableCommand(this));
        this.addSub(new WorldEnableCommand(this));
        this.addSub(new WorldListCommand(this));
        this.addSub(new WorldLoadCommand(this));
        this.addSub(new WorldUnloadCommand(this));
        this.addSub(new WorldTpCommand(this));
        this.addSub(new WorldSetMainCommand(this));
        this.addSub(new WorldSetSpawnCommand(this));
    }
}
