package com.dragonite.mc.dnmc.core.managers.builder;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface AbstractInventoryBuilder extends Buildable<Inventory> {
    /**
     * 第一行
     */
    int ONE_ROW = 9;

    /**
     * 中央位置
     */
    int CENTER = 4;

    /**
     * 初始位置
     */
    int START = 0;

    /**
     * 尾部位置
     */
    int END = 8;

    /**
     * @param item 物品
     * @return this
     */
    AbstractInventoryBuilder item(ItemStack item);

    /**
     * @param slot 槽位
     * @param item 物品
     * @return this
     */
    AbstractInventoryBuilder item(int slot, ItemStack item);

    /**
     * start from row 1, slot 0
     *
     * @param row  行數
     * @param slot 欄位
     * @param item 物品
     * @return this
     */
    AbstractInventoryBuilder item(int row, int slot, ItemStack item);

    /**
     * 設置中央位置
     * <br>
     * 若果你是雙數行，則無法找出中央位置
     *
     * @param item 物品
     * @return this
     */
    AbstractInventoryBuilder center(ItemStack item);

    /**
     * 設置環狀圍繞
     *
     * @param item 物品
     * @return this
     * @throws IllegalStateException 背包界面少於三行
     */
    AbstractInventoryBuilder ring(ItemStack item);

    /**
     * 填滿一整行
     *
     * @param row  行數
     * @param item 物品
     * @return this
     */
    AbstractInventoryBuilder fillRow(int row, ItemStack item);

}
