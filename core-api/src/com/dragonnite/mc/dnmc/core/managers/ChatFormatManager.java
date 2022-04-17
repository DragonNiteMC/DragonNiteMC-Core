package com.dragonnite.mc.dnmc.core.managers;

import org.bukkit.entity.Player;

public interface ChatFormatManager {

    /**
     * @param player 玩家
     * @return 帶有訊息的聊天格式, 沒有時返回空白
     */
    String getChatFormat(Player player);

    /**
     * @param player 玩家
     * @return 不帶有訊息的聊天格式, 沒有時返回空白
     */
    String getFormat(Player player);

    /**
     * 更新 tab list
     *
     * @param player 玩家
     */
    void updatePlayerList(Player player);
}
