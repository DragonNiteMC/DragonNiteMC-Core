package com.dragonnite.mc.dnmc.core.factory.builder;

import com.dragonnite.mc.dnmc.core.managers.builder.AbstractAdvMessageBuilder;
import com.dragonnite.mc.dnmc.core.managers.builder.AbstractMessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public final class AdvMessageFactory implements AbstractAdvMessageBuilder {

    private TextComponent textComponent;

    public AdvMessageFactory(String... msg) {
        this.textComponent = new TextComponent();
        this.add(msg);
    }

    @Override
    public AbstractAdvMessageBuilder add(String... msg) {
        for (String s : msg) {
            var text = ChatColor.translateAlternateColorCodes('&', s);
            this.textComponent.addExtra(text);
        }
        return this;
    }

    @Override
    public AbstractAdvMessageBuilder add(BaseComponent... baseComponent) {
        for (var base : baseComponent) {
            this.textComponent.addExtra(base);
        }
        return this;
    }


    @Override
    public AbstractAdvMessageBuilder add(AbstractMessageBuilder... builders) {
        for (AbstractMessageBuilder builder : builders) {
            for (BaseComponent com : builder.build()) {
                this.textComponent.addExtra(com);
            }
        }
        return this;
    }

    @Override
    public AbstractAdvMessageBuilder nextLine() {
        this.textComponent.addExtra("\n");
        return this;
    }

    @Override
    public void sendPlayer(Player player) {
        player.sendMessage(textComponent);
    }

    @Override
    public TextComponent build() {
        return textComponent;
    }
}
