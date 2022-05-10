package com.dragonite.mc.dnmc.core.managers;

public interface CoreConfig {

    /**
     * @return 前綴
     */
    String getPrefix();

    /**
     * @return 無權限訊息
     */
    String getNoPerm();

    /**
     * @return 不是玩家訊息
     */
    String getNotPlayer();

    /**
     * @return 找不到玩家訊息
     */
    String getNotFoundPlayer();
}
