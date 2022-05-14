package com.dragonite.mc.dnmc.core.builders;

import com.dragonite.mc.dnmc.core.main.DragoniteMC;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractAdvMessageBuilder;
import com.dragonite.mc.dnmc.core.managers.builder.AbstractMessageBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * @see MessageBuilder
 */
public class AdvMessageBuilder implements AbstractAdvMessageBuilder {

    private final AbstractAdvMessageBuilder delegate;

    public AdvMessageBuilder(String... msg) {
        this.delegate = DragoniteMC.getAPI().getFactory().getBuilder().getAdvMessageBuilder(msg);
    }

    @Override
    public AbstractAdvMessageBuilder add(String... msg) {
        return delegate.add(msg);
    }

    @Override
    public AbstractAdvMessageBuilder add(BaseComponent... baseComponent) {
        return delegate.add(baseComponent);
    }

    @Override
    public AbstractAdvMessageBuilder add(AbstractMessageBuilder... builders) {
        return delegate.add(builders);
    }

    @Override
    public AbstractAdvMessageBuilder nextLine() {
        return delegate.nextLine();
    }

    @Override
    public void sendPlayer(Player player) {
        delegate.sendPlayer(player);
    }

    @Override
    public TextComponent build() {
        return delegate.build();
    }
}
