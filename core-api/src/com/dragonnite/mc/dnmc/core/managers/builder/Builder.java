package com.dragonnite.mc.dnmc.core.managers.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Builder {

    /**
     * @param msg 訊息串
     * @return 訊息建造器
     */
    AbstractAdvMessageBuilder getAdvMessageBuilder(String... msg);

    /**
     * @param row   此背包界面最大行數
     * @param title 標題
     * @return 界面建造器
     */
    AbstractInventoryBuilder getInventoryBuilder(int row, String title);

    /**
     * @param msg 原始訊息串
     * @return 訊息建造器
     */
    AbstractMessageBuilder getMessageBuilder(String... msg);

    /**
     * @return 物品建造器
     */
    AbstractItemStackBuilder getItemStackBuilder();

    /**
     * @param material 物品類型
     * @return 物品建造器
     */
    AbstractItemStackBuilder getItemStackBuilder(Material material);

    /**
     * @param item 原始物品
     * @return 物品建造器
     */
    AbstractItemStackBuilder getItemStackBuilder(ItemStack item);

}
