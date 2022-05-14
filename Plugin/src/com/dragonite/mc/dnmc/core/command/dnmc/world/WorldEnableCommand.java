package com.dragonite.mc.dnmc.core.command.dnmc.world;

import com.dragonite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonite.mc.dnmc.core.misc.world.WorldLoadedException;
import com.dragonite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldEnableCommand extends WorldCommandNode {

    public WorldEnableCommand(CommandNode parent) {
        super(parent, "enable", "dragonite.world.enable", "開啟該世界的自動加載", "<world>");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        String name = args.get(0);
        try {
            sender.sendMessage(prefix + "正在啟用並加載世界...");
            DragoniteMC.getDnmcWorldManager().enableWorld(name).whenComplete((result, ex) -> {
                if (ex != null) ex.printStackTrace();
                sender.sendMessage(prefix + "世界 " + name + " 啟用自動加載 " + (result ? "成功並已加載" : "失敗") + "。");
            });

        } catch (WorldNonExistException e) {
            sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§c世界 " + e.getWorld() + " 不存在!");
        } catch (IllegalStateException e) {
            sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§c該世界沒有被關閉自動加載。");
        } catch (WorldLoadedException e) {
            sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§a世界已被加載。");
        }
        return false;
    }

}
