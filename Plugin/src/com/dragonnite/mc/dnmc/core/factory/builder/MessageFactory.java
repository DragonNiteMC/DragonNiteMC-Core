package com.dragonnite.mc.dnmc.core.factory.builder;

import com.dragonnite.mc.dnmc.core.config.serializer.NMSUtils;
import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.builder.AbstractMessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Consumer;

public final class MessageFactory implements AbstractMessageBuilder {

    private ComponentBuilder componentBuilder;
    private UUID id;
    private Consumer<Player> runner;
    private int timeoutSeconds = -1;
    private int timeoutClicks = -1;

    public MessageFactory(String... msg) {
        this.componentBuilder = new ComponentBuilder("");
        this.add(msg);
    }

    @Override
    public AbstractMessageBuilder add(String... msg) {
        for (int i = 0; i < msg.length; i++) {
            var txt = ChatColor.translateAlternateColorCodes('&', msg[i]);
            componentBuilder.append(TextComponent.fromLegacyText(txt));
            if (i != msg.length - 1) {
                this.nextLine();
            }
        }
        return this;
    }

    @Override
    public AbstractMessageBuilder url(String website) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, website));
        return this;
    }

    @Override
    public AbstractMessageBuilder suggest(String command) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    @Override
    public AbstractMessageBuilder command(String command) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    @Override
    public AbstractMessageBuilder page(String page) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));
        return this;
    }

    @Override
    public AbstractMessageBuilder hoverText(String... texts) {
        var builder = new MessageFactory(texts);
        componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, builder.build()));
        return this;
    }

    @Override
    public AbstractMessageBuilder showItem(ItemStack item) {
        String json = NMSUtils.convertItemStackToJson(item);
        if (json == null) {
            DragonNiteMC.plugin.getLogger().warning("Item show for " + item.getType() + "is invalid");
            return this;
        }
        componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_ITEM, TextComponent.fromLegacyText(json)));
        return this;
    }

    @Override
    public AbstractMessageBuilder showEntity(Entity entity) {
        String json = NMSUtils.convertEntityToJson(entity);
        if (json == null) {
            DragonNiteMC.plugin.getLogger().warning("Item show for " + entity.getType() + "is invalid");
            return this;
        }
        componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_ENTITY, TextComponent.fromLegacyText(json)));
        return this;
    }

    @Override
    public AbstractMessageBuilder showAdvancement(String achievementNode) {
        String value = "\"value\":" + achievementNode;
        componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(value)));
        return this;
    }

    @Override
    public AbstractMessageBuilder insertWhenShiftClick(String insert) {
        componentBuilder.insertion(insert);
        return this;
    }

    @Override
    public AbstractMessageBuilder run(Consumer<Player> runner) {
        this.id = UUID.randomUUID();
        this.runner = runner;
        this.timeoutSeconds = 600;
        this.timeoutClicks = -1;
        return this;
    }

    @Override
    public AbstractMessageBuilder runClicks(int timeoutClicks, Consumer<Player> runner) {
        this.id = UUID.randomUUID();
        this.runner = runner;
        this.timeoutClicks = timeoutClicks;
        this.timeoutSeconds = -1;
        return this;
    }

    @Override
    public AbstractMessageBuilder runTimeout(int timeoutSeconds, Consumer<Player> runner) {
        this.id = UUID.randomUUID();
        this.runner = runner;
        this.timeoutSeconds = timeoutSeconds;
        this.timeoutClicks = -1;
        return this;
    }

    @Override
    public AbstractMessageBuilder nextLine() {
        componentBuilder.append("\n");
        return this;
    }

    @Override
    public void sendPlayer(Player player) {
        player.sendMessage(this.build());
    }

    @Override
    public BaseComponent[] build() {
        if (this.runner != null) {
            this.command("/command-run_" + id.toString());
            if (timeoutSeconds > 0) {
                DragonNiteMC.getChatRunnerHandler().registerTimeout(id, runner, timeoutSeconds);
            } else if (timeoutClicks > 0) {
                DragonNiteMC.getChatRunnerHandler().registerClicks(id, runner, timeoutClicks);
            }
        }
        return componentBuilder.create();
    }
}
