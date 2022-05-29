package com.dragonite.mc.dnmc.core.command.dnmc.world;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.world.WorldNonExistException;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorldCheckCommand extends WorldCommandNode{

    public WorldCheckCommand(CommandNode parent) {
        super(parent, "check", "dragonite.world.check", "檢查世界內容", "<world>");
    }

    @Override
    public boolean executeCommand(@NotNull CommandSender sender, @NotNull List<String> args) {
        String world = args.get(0);
        try {
            var worldInfo = DragoniteMC.getAPI().getWorldManager().getWorldProperties(world);
            sender.sendMessage("§a世界 §e" + world + " §a的資訊如下：");
            sender.sendMessage("§a- 允許PVP：§e" + worldInfo.isPvp());
            sender.sendMessage("§a- 允許PVE：§e" + worldInfo.isPve());
            sender.sendMessage("§a- 自動加載：§e" + worldInfo.isAutoLoad());
            sender.sendMessage("§a- 無敵：§e" + !worldInfo.isVulnerable());
        }catch (WorldNonExistException e){
            sender.sendMessage(prefix + "§c世界 " + e.getWorld() + " 不存在!");
        }
        return true;
    }
}
