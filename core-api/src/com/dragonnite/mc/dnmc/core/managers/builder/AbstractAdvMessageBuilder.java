package com.dragonnite.mc.dnmc.core.managers.builder;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * @see AbstractMessageBuilder
 */
public interface AbstractAdvMessageBuilder extends Buildable<TextComponent> {

    AbstractAdvMessageBuilder add(String... msg);

    AbstractAdvMessageBuilder add(BaseComponent... baseComponent);

    AbstractAdvMessageBuilder add(AbstractMessageBuilder... builders);

    AbstractAdvMessageBuilder nextLine();

    void sendPlayer(Player player);

}
