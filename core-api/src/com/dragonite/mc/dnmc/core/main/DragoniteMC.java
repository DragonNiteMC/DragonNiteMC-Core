package com.dragonite.mc.dnmc.core.main;

import com.dragonite.mc.dnmc.core.DragoniteMCAPI;
import com.dragonite.mc.dnmc.core.factory.CoreFactory;
import com.dragonite.mc.dnmc.core.managers.*;

/**
 * 從這裏獲取 API
 */
public class DragoniteMC implements DragoniteMCAPI {

    public static DragoniteMCAPI getAPI() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public BungeeManager getBungeeManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public ChatFormatManager getChatFormatManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public CoreFactory getFactory() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public CoreScheduler getCoreScheduler() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public CommandRegister getCommandRegister() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public NameTagManager getNameTagManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public PlayerSkinManager getPlayerSkinManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public SQLDataSource getSQLDataSource() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public RedisDataSource getRedisDataSource() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public TabListManager getTabListManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public CoreConfig getCoreConfig() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public WorldManager getWorldManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public EventCancelManager getEventCancelManager() {
        throw new RuntimeException("RUNTIME ERROR");
    }

    @Override
    public ResourceManager getResourceManager(ResourceManager.Type type) {
        throw new RuntimeException("RUNTIME ERROR");
    }
}
