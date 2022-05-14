package com.dragonite.mc.dnmc.core.managers;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public interface BungeeManager {

    /**
     * @param player 玩家
     * @param server 伺服器名稱
     */
    void sendPlayer(@Nonnull Player player, String server);

    /**
     * @param server 伺服器
     */
    void sendAllPlayers(String server);

    /**
     * 等待玩家全數傳送到該服後再自動關服
     *
     * @param server 伺服器
     */
    void sendBeforeStop(String server);

    /**
     * 等待玩家全數傳送到該服後再自動重啟
     *
     * @param server 伺服器
     */
    void sendBeforeRestart(String server);
}
