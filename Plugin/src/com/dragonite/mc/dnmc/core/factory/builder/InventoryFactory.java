package com.dragonite.mc.dnmc.core.factory.builder;

import com.dragonite.mc.dnmc.core.managers.builder.AbstractInventoryBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class InventoryFactory implements AbstractInventoryBuilder {

    private final Inventory inventory;
    private final int row;

    public InventoryFactory(int row, String title) {
        this.row = Math.min(row, 6);
        String colorTitle = ChatColor.translateAlternateColorCodes('&', title);
        inventory = Bukkit.createInventory(null, ONE_ROW * this.row, colorTitle);
    }

    @Override
    public AbstractInventoryBuilder item(ItemStack item) {
        inventory.addItem(item);
        return this;
    }

    @Override
    public AbstractInventoryBuilder item(int slot, ItemStack item) {
        inventory.setItem(slot, item);
        return this;
    }

    @Override
    public AbstractInventoryBuilder item(int row, int slot, ItemStack item) {
        inventory.setItem((row - 1) * ONE_ROW + slot, item);
        return this;
    }

    @Override
    public AbstractInventoryBuilder center(ItemStack item) {
        int realRow = row % 2 == 0 ? row / 2 : row / 2 + 1;
        this.item(realRow, CENTER, item);
        return this;
    }

    @Override
    public AbstractInventoryBuilder ring(ItemStack item) {
        if (this.row < 3) {
            throw new IllegalStateException("若要使用物品環繞，你的界面最少要擁有三行。");
        }
        for (int i = 1; i < this.row + 1; i++) {
            if (i == 1 || i == this.row) {
                this.fillRow(i, item);
            } else {
                this.item(i, START, item);
                this.item(i, END, item);
            }
        }
        return this;
    }

    @Override
    public AbstractInventoryBuilder fillRow(int row, ItemStack item) {
        for (int i = 0; i < ONE_ROW; i++) {
            this.item(row, i, item);
        }
        return this;
    }

    @Override
    public Inventory build() {
        return inventory;
    }
}
