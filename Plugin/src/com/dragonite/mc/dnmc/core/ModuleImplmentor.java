package com.dragonite.mc.dnmc.core;

import com.dragonite.mc.dnmc.core.bungeecord.Bungee;
import com.dragonite.mc.dnmc.core.caxerx.CommandHandler;
import com.dragonite.mc.dnmc.core.caxerx.ItemBuilderEventListener;
import com.dragonite.mc.dnmc.core.chatformat.ChatFormatListener;
import com.dragonite.mc.dnmc.core.chatformat.FormatDatabaseManager;
import com.dragonite.mc.dnmc.core.chatformat.NameTagHandler;
import com.dragonite.mc.dnmc.core.command.HelpCommand;
import com.dragonite.mc.dnmc.core.config.SchedulerManager;
import com.dragonite.mc.dnmc.core.config.implement.DNMCoreConfig;
import com.dragonite.mc.dnmc.core.ericlam.ChatRunnerHandler;
import com.dragonite.mc.dnmc.core.ericlam.TablistBuilder;
import com.dragonite.mc.dnmc.core.factory.CoreFactory;
import com.dragonite.mc.dnmc.core.factory.DNMainFactory;
import com.dragonite.mc.dnmc.core.factory.builder.FactoryBuilder;
import com.dragonite.mc.dnmc.core.listener.cancelevent.CancelEventManager;
import com.dragonite.mc.dnmc.core.managers.*;
import com.dragonite.mc.dnmc.core.managers.builder.Builder;
import com.dragonite.mc.dnmc.core.mysql.SQLDataSourceManager;
import com.dragonite.mc.dnmc.core.skin.PlayerSkinHandler;
import com.dragonite.mc.dnmc.core.updater.DragoniteResourceManager;
import com.dragonite.mc.dnmc.core.updater.SpigotResourceManager;
import com.dragonite.mc.dnmc.core.listener.EventListener;
import com.dragonite.mc.dnmc.core.worlds.BukkitWorldHandler;
import com.dragonite.mc.dnmc.core.worlds.WorldPropertiesManager;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ModuleImplmentor implements Module {

    private final Map<Class, Object> preImplement = new HashMap<>();

    @Override
    public void configure(Binder binder) {
        binder.bind(BungeeManager.class).to(Bungee.class).in(Scopes.SINGLETON);
        binder.bind(ChatFormatManager.class).to(Format.class).in(Scopes.SINGLETON);
        binder.bind(CoreConfig.class).to(DNMCoreConfig.class).in(Scopes.SINGLETON);
        binder.bind(CommandRegister.class).to(CommandHandler.class).in(Scopes.SINGLETON);
        binder.bind(CoreScheduler.class).to(SchedulerManager.class).in(Scopes.SINGLETON);
        binder.bind(NameTagManager.class).to(NameTagHandler.class).in(Scopes.SINGLETON);
        binder.bind(PlayerSkinManager.class).to(PlayerSkinHandler.class).in(Scopes.SINGLETON);
        binder.bind(SQLDataSource.class).to(SQLDataSourceManager.class).in(Scopes.SINGLETON);
        binder.bind(TabListManager.class).to(TablistBuilder.class).in(Scopes.SINGLETON);
        binder.bind(WorldManager.class).to(BukkitWorldHandler.class).in(Scopes.SINGLETON);
        binder.bind(CoreFactory.class).to(DNMainFactory.class).in(Scopes.SINGLETON);
        binder.bind(Builder.class).to(FactoryBuilder.class).in(Scopes.SINGLETON);
        binder.bind(RedisDataSource.class).to(RedisManager.class).in(Scopes.SINGLETON);
        binder.bind(EventCancelManager.class).to(CancelEventManager.class).in(Scopes.SINGLETON);

        /*
            Not API use but for singleton
         */
        binder.bind(WorldPropertiesManager.class).in(Scopes.SINGLETON);
        binder.bind(SkinDatabaseManager.class).in(Scopes.SINGLETON);
        binder.bind(HelpPagesManager.class).in(Scopes.SINGLETON);
        binder.bind(FormatDatabaseManager.class).in(Scopes.SINGLETON);
        binder.bind(ChatRunnerHandler.class).in(Scopes.SINGLETON);
        binder.bind(ItemBuilderEventListener.class).in(Scopes.SINGLETON);
        binder.bind(SpigotResourceManager.class).in(Scopes.SINGLETON);
        binder.bind(DragoniteResourceManager.class).in(Scopes.SINGLETON);


        /*
            Not API use but for listener / commands
         */
        binder.bind(EventListener.class).in(Scopes.NO_SCOPE);
        binder.bind(ChatFormatListener.class).in(Scopes.NO_SCOPE);
        binder.bind(HelpCommand.class).in(Scopes.NO_SCOPE);

        preImplement.forEach((c, o) -> binder.bind(c).toInstance(o));
    }

    public <T> void register(Class<T> cls, T  obj) {
        preImplement.put(cls, obj);
    }
}
