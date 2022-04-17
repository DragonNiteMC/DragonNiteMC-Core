package com.dragonnite.mc.dnmc.core.managers;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public interface VaultAPI {

    /**
     * @return Vault 經濟 API
     */
    Economy getEconomy();

    /**
     * @return Vault 聊天 API
     */
    Chat getChat();

    /**
     * @return Vault 權限 API
     */
    Permission getPermission();
}
