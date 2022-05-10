package com.dragonnite.mc.dnmc.core;

import com.dragonnite.mc.dnmc.core.factory.CoreFactory;
import com.dragonnite.mc.dnmc.core.managers.*;

/**
 * Manager API 獲取器
 */
public interface DragonNiteMCAPI {

    /**
     * @return Bungee 管理器
     */
    BungeeManager getBungeeManager();

    /**
     * @return 聊天格式管理器
     */
    ChatFormatManager getChatFormatManager();

    /**
     * @return 工廠
     */
    CoreFactory getFactory();

    /**
     * @return api主導的排程器
     */
    CoreScheduler getCoreScheduler();

    /**
     * @return 註冊指令
     */
    CommandRegister getCommandRegister();

    /**
     * @return nameTag管理
     */
    NameTagManager getNameTagManager();

    /**
     * @return 玩家皮膚管理器
     */
    PlayerSkinManager getPlayerSkinManager();

    /**
     * @return SQL 管理器
     */
    SQLDataSource getSQLDataSource();

    /**
     * @return Redis 管理器
     */
    RedisDataSource getRedisDataSource();

    /**
     * @return TabList 管理
     */
    TabListManager getTabListManager();

    /**
     * @return api 文件
     */
    CoreConfig getCoreConfig();

    /**
     * @return 世界管理
     */
    WorldManager getWorldManager();

    /**
     * 主要用於 CancelEvent.yml 的事件取消處理。
     *
     * @return 事件取消管理器
     */
    EventCancelManager getEventCancelManager();

    /**
     * 插件版本管理
     * @return 插件版本管理器
     * @param type 管理器類別
     */
    ResourceManager getResourceManager(ResourceManager.Type type);

}
