package com.dragonnite.mc.dnmc.core.command.dnmc.world;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.world.WorldExistException;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldCreateVoidCommand extends WorldCommandNode {

    public WorldCreateVoidCommand(CommandNode parent) {
        super(parent, "createvoid", "dragonnite.world.createvoid", "創建虛空世界", "<world>", "void", "cv");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        final String worldName = args.get(0);
        try {
            sender.sendMessage(DragonNiteMC.getDnmCoreConfig().getPrefix() + "§e正在創建虛空世界....");
            DragonNiteMC.getDnmcWorldManager().createVoidWorld(worldName).whenComplete((w, ex) -> {
                if (ex != null) ex.printStackTrace();
                var result = w != null;
                sender.sendMessage(DragonNiteMC.getDnmCoreConfig().getPrefix() + "§e世界 " + worldName + " 創建 " + (result ? "成功" : "失敗") + "。");
            });
        } catch (WorldExistException e) {
            sender.sendMessage(DragonNiteMC.getAPI().getCoreConfig().getPrefix() + "§c世界已存在。");
        }
        return true;
    }
}
