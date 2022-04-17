package com.dragonnite.mc.dnmc.core.managers;

import org.bukkit.entity.Player;

public interface TabListManager {

    /**
     * @param header TabList 上方標題
     * @param player 玩家
     */
    void setHeader(String header, Player player);

    /**
     * @param header TabList 上方標題
     * @param footer TabList 下方標題
     * @param player 玩家
     */
    void setHeaderFooter(String header, String footer, Player player);

}
