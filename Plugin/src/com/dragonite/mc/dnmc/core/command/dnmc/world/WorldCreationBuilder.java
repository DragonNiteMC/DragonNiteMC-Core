package com.dragonite.mc.dnmc.core.command.dnmc.world;

import com.dragonite.mc.dnmc.core.builders.AdvMessageBuilder;
import com.dragonite.mc.dnmc.core.builders.MessageBuilder;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractMessageBuilder;
import com.dragonite.mc.dnmc.core.misc.world.WorldExistException;
import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

class WorldCreationBuilder {

    private final String world;
    private World.Environment environment;
    private WorldType worldType;
    private boolean generateStructure = true;

    WorldCreationBuilder(String world, Player player) {
        this.world = world;
        this.getEnvironmentSelection(player);
    }

    WorldCreationBuilder(String world, boolean genStructures, Player player) {
        this(world, player);
        this.generateStructure = genStructures;
    }

    private void setEnvironment(World.Environment environment, Player player) {
        if (this.environment != null) {
            player.sendMessage(ChatColor.RED + "你已經選擇過了");
            return;
        }
        this.environment = environment;
        getTypeSelection(player);
    }

    private void getEnvironmentSelection(Player sender) {
        AbstractMessageBuilder title = new MessageBuilder("&7請選擇世界 [" + world + "] 的環境類別: ");
        AbstractMessageBuilder normal = new MessageBuilder("&e[ 普通世界 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setEnvironment(World.Environment.NORMAL, player);
        });
        AbstractMessageBuilder nether = new MessageBuilder("&e[ 地獄世界 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setEnvironment(World.Environment.NETHER, player);
        });
        AbstractMessageBuilder end = new MessageBuilder("&e[ 終界世界 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setEnvironment(World.Environment.THE_END, player);
        });
        new AdvMessageBuilder().add(title, normal, nether, end).sendPlayer(sender);
    }

    private void setWorldType(WorldType type, Player player) {
        if (this.worldType != null) {
            player.sendMessage(ChatColor.RED + "你已經選擇過了");
            return;
        }
        this.worldType = type;
        this.confirmCreation(player);

    }

    private void getTypeSelection(Player sender) {
        AbstractMessageBuilder title = new MessageBuilder("&7請選擇世界 [" + world + "] 的地形類別");
        AbstractMessageBuilder normal = new MessageBuilder("&e[ 普通地形 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setWorldType(WorldType.NORMAL, player);
        });
        AbstractMessageBuilder amplified = new MessageBuilder("&e[ 巨大化世界 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setWorldType(WorldType.AMPLIFIED, player);
        });
        AbstractMessageBuilder large_biomes = new MessageBuilder("&e[ 大型生態域 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setWorldType(WorldType.LARGE_BIOMES, player);
        });
        AbstractMessageBuilder flat = new MessageBuilder("&e[ 超平坦世界 ]").hoverText("&d點擊選擇").runTimeout(30, player -> {
            this.setWorldType(WorldType.FLAT, player);
        });
        new AdvMessageBuilder("").add(title, normal, amplified, large_biomes, flat).sendPlayer(sender);
    }

    private void confirmCreation(Player sender) {
        sender.sendMessage("§7 你的世界 構建資料如下: ");
        sender.sendMessage("§e世界名稱： §7" + world);
        sender.sendMessage("§e環境: §7" + environment.toString());
        sender.sendMessage("§e地形: §7" + worldType.getName());
        sender.sendMessage("§e生成建築: §7" + generateStructure);
        sender.sendMessage("§a是否創建 ?");
        new MessageBuilder("&e[ 確定創建 ]").hoverText("&d點擊確認").runClicks(1, player -> {
            try {
                DragoniteMC.getDnmcWorldManager().createWorld(world, worldType, environment, generateStructure).whenComplete((w, ex) -> {
                    if (ex != null) ex.printStackTrace();
                    var result = w != null;
                    sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§e世界 " + world + " 創建 " + (result ? "成功" : "失敗") + "。");
                });
                sender.sendMessage(DragoniteMC.getDnmCoreConfig().getPrefix() + "§e正在創建世界....");
            } catch (WorldExistException e) {
                sender.sendMessage(DragoniteMC.getAPI().getCoreConfig().getPrefix() + "§c世界已存在。");
            }
        }).sendPlayer(sender);
    }
}
