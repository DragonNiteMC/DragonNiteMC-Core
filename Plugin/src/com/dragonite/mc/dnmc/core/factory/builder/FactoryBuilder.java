package com.dragonite.mc.dnmc.core.factory.builder;

import com.dragonite.mc.dnmc.core.managers.builder.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public final class FactoryBuilder implements Builder {

    @Override
    public AbstractAdvMessageBuilder getAdvMessageBuilder(String... msg) {
        return new AdvMessageFactory(msg);
    }

    @Override
    public AbstractInventoryBuilder getInventoryBuilder(int row, String title) {
        return new InventoryFactory(row, title);
    }

    @Override
    public AbstractMessageBuilder getMessageBuilder(String... msg) {
        return new MessageFactory(msg);
    }

    @Override
    public AbstractItemStackBuilder getItemStackBuilder() {
        return new ItemStackFactory();
    }

    @Override
    public AbstractItemStackBuilder getItemStackBuilder(Material material) {
        return new ItemStackFactory(material);
    }

    @Override
    public AbstractItemStackBuilder getItemStackBuilder(ItemStack item) {
        return new ItemStackFactory(item);
    }
}
