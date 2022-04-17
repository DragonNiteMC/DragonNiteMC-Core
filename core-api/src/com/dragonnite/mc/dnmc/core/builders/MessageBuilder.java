package com.dragonnite.mc.dnmc.core.builders;

import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.builder.AbstractMessageBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * @see AbstractMessageBuilder
 */
public class MessageBuilder implements AbstractMessageBuilder {

    private final AbstractMessageBuilder delegate;

    /**
     * @param msg 原始訊息
     */
    public MessageBuilder(String... msg) {
        this.delegate = DragonNiteMC.getAPI().getFactory().getBuilder().getMessageBuilder(msg);
    }

    public MessageBuilder() {
        this.delegate = DragonNiteMC.getAPI().getFactory().getBuilder().getMessageBuilder();
    }

    @Override
    public AbstractMessageBuilder add(String... msg) {
        return delegate.add(msg);
    }

    @Override
    public AbstractMessageBuilder url(String website) {
        return delegate.url(website);
    }

    @Override
    public AbstractMessageBuilder suggest(String command) {
        return delegate.suggest(command);
    }

    @Override
    public AbstractMessageBuilder command(String command) {
        return delegate.command(command);
    }

    @Override
    public AbstractMessageBuilder page(String page) {
        return delegate.page(page);
    }

    @Override
    public AbstractMessageBuilder hoverText(String... texts) {
        return delegate.hoverText(texts);
    }

    @Override
    public AbstractMessageBuilder showItem(ItemStack item) {
        return delegate.showItem(item);
    }

    @Override
    public AbstractMessageBuilder showEntity(Entity entity) {
        return delegate.showEntity(entity);
    }

    @Override
    public AbstractMessageBuilder showAdvancement(String achievementNode) {
        return delegate.showAdvancement(achievementNode);
    }

    @Override
    public AbstractMessageBuilder insertWhenShiftClick(String insert) {
        return delegate.insertWhenShiftClick(insert);
    }

    @Override
    public AbstractMessageBuilder run(Consumer<Player> runner) {
        return delegate.run(runner);
    }

    @Override
    public AbstractMessageBuilder runClicks(int timeoutClicks, Consumer<Player> runner) {
        return delegate.runClicks(timeoutClicks, runner);
    }

    @Override
    public AbstractMessageBuilder runTimeout(int timeoutSeconds, Consumer<Player> runner) {
        return delegate.runTimeout(timeoutSeconds, runner);
    }

    @Override
    public AbstractMessageBuilder nextLine() {
        return delegate.nextLine();
    }

    @Override
    public void sendPlayer(Player player) {
        delegate.sendPlayer(player);
    }

    @Override
    public BaseComponent[] build() {
        return delegate.build();
    }
}

