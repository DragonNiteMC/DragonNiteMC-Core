package com.dragonnite.mc.dnmc.core.builders;

import com.dragonnite.mc.dnmc.core.main.DragonNiteMC;
import com.dragonnite.mc.dnmc.core.managers.builder.AbstractInventoryBuilder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * @see AbstractInventoryBuilder
 */
public class InventoryBuilder implements AbstractInventoryBuilder {
    /**
     * 第一行
     */
    public static final int ONE_ROW = AbstractInventoryBuilder.ONE_ROW;
    /**
     * 中央位置
     */
    public static final int CENTER = AbstractInventoryBuilder.CENTER;
    /**
     * 初始位置
     */
    public static final int START = AbstractInventoryBuilder.START;
    /**
     * 尾部位置
     */
    public static final int END = AbstractInventoryBuilder.END;


    private final AbstractInventoryBuilder delegate;

    /**
     * @param row   此背包界面最大行數
     * @param title 標題
     */
    public InventoryBuilder(int row, String title) {
        this.delegate = DragonNiteMC.getAPI().getFactory().getBuilder().getInventoryBuilder(row, title);
    }


    @Override
    public AbstractInventoryBuilder item(ItemStack item) {
        return delegate.item(item);
    }


    @Override
    public AbstractInventoryBuilder item(int slot, ItemStack item) {
        return delegate.item(slot, item);
    }


    @Override
    public AbstractInventoryBuilder item(int row, int slot, ItemStack item) {
        return delegate.item(row, slot, item);
    }


    @Override
    public AbstractInventoryBuilder center(ItemStack item) {
        return delegate.center(item);
    }


    @Override
    public AbstractInventoryBuilder ring(ItemStack item) {
        return delegate.ring(item);
    }


    @Override
    public AbstractInventoryBuilder fillRow(int row, ItemStack item) {
        return delegate.fillRow(row, item);
    }

    @Override
    public Inventory build() {
        return delegate.build();
    }
}