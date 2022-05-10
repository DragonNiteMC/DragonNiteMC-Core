package com.dragonite.mc.dnmc.core.command.dnmc.world;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.world.WorldExistException;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldCreateVoidCommand extends WorldCommandNode {

    public WorldCreateVoidCommand(CommandNode parent) {
        super(parent, "createvoid", "dragonite.world.createvoid", "創建虛空世界", "<world>", "void", "cv");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        final String worldName = args.get(0);
        try {
            sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§e正在創建虛空世界....");
            DragoniteMC.getDnmcWorldManager().createVoidWorld(worldName).whenComplete((w, ex) -> {
                if (ex != null) ex.printStackTrace();
                var result = w != null;
                sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§e世界 " + worldName + " 創建 " + (result ? "成功" : "失敗") + "。");
            });
        } catch (WorldExistException e) {
            sender.sendMessage(DragoniteMC.getAPI().getCoreConfig().getPrefix() + "§c世界已存在。");
        }
        return true;
    }
}
