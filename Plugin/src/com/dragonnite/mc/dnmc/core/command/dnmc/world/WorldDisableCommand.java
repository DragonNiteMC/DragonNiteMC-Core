package com.dragonnite.mc.dnmc.core.command.dnmc.world;

import com.dragonnite.mc.dnmc.core.misc.commands.CommandNode;
import com.dragonnite.mc.dnmc.core.misc.world.WorldNonExistException;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.List;

public class WorldDisableCommand extends WorldCommandNode {

    public WorldDisableCommand(CommandNode parent) {
        super(parent, "disable", "dragonnite.world.disable", "關閉該世界的自動加載", "<world>");
    }

    @Override
    public boolean executeCommand(@Nonnull CommandSender sender, @Nonnull List<String> args) {
        String name = args.get(0);
        if (WorldUnloadCommand.validateDefaultWorld(name)) {
            sender.sendMessage(prefix + "§c你無法禁用默認世界。");
            return false;
        }
        try {
            boolean success = DragonNiteMC.getDnmcWorldManager().disableWorld(name);
            sender.sendMessage(prefix + "世界 " + name + " 關閉自動加載 " + (success ? "成功" : "失敗") + "。");
        } catch (WorldNonExistException e) {
            sender.sendMessage(prefix + "§c世界 " + e.getWorld() + " 不存在!");
        } catch (IllegalStateException e) {
            sender.sendMessage(prefix + "§c該世界目前已關閉了自動加載。");
        }
        return false;
    }
}
