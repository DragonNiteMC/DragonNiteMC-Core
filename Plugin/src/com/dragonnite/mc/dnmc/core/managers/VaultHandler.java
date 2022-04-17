package com.dragonnite.mc.dnmc.core.managers;

import com.google.inject.Inject;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;

public class VaultHandler implements VaultAPI {

    @Inject
    private Plugin plugin;

    @Override
    public Economy getEconomy() {
        Optional<RegisteredServiceProvider<Economy>> providerOptional = Optional.ofNullable(plugin.getServer().getServicesManager().getRegistration(Economy.class));
        RegisteredServiceProvider<Economy> provider = providerOptional.orElseThrow(() -> new IllegalStateException("我們找不到支援 Vault 的經濟插件！！"));
        return provider.getProvider();
    }

    @Override
    public Chat getChat() {
        Optional<RegisteredServiceProvider<Chat>> providerOptional = Optional.ofNullable(plugin.getServer().getServicesManager().getRegistration(Chat.class));
        RegisteredServiceProvider<Chat> provider = providerOptional.orElseThrow(() -> new IllegalStateException("我們找不到支援 Vault 的聊天插件！！"));
        return provider.getProvider();
    }

    @Override
    public Permission getPermission() {
        Optional<RegisteredServiceProvider<Permission>> providerOptional = Optional.ofNullable(plugin.getServer().getServicesManager().getRegistration(Permission.class));
        RegisteredServiceProvider<Permission> provider = providerOptional.orElseThrow(() -> new IllegalStateException("我們找不到支援 Vault 的權限插件！！"));
        return provider.getProvider();
    }
}
