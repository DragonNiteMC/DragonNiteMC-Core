package com.dragonite.mc.dnmc.core.managers.builder;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface AbstractMessageBuilder extends Buildable<BaseComponent[]> {

    /**
     * @param msg 訊息串
     * @return this
     */
    AbstractMessageBuilder add(String... msg);

    /**
     * @param website 可點擊連結
     * @return this
     */
    AbstractMessageBuilder url(String website);

    /**
     * @param command 指令輸入
     * @return this
     */
    AbstractMessageBuilder suggest(String command);

    /**
     * @param command 指令執行
     * @return this
     */
    AbstractMessageBuilder command(String command);

    /**
     * @param page 頁數
     * @return this
     */
    AbstractMessageBuilder page(String page);

    /**
     * @param texts 文字氣泡
     * @return this
     */
    AbstractMessageBuilder hoverText(String... texts);

    /**
     * 顯示物品資訊
     *
     * @param item 物品
     * @return this
     */
    AbstractMessageBuilder showItem(ItemStack item);

    /**
     * 顯示實體資訊
     *
     * @param entity 實體
     * @return this
     */
    AbstractMessageBuilder showEntity(Entity entity);

    /**
     * 顯示成就資訊
     *
     * @param achievementNode 成就節點
     * @return this
     */
    AbstractMessageBuilder showAdvancement(String achievementNode);

    /**
     * @param insert 訊息
     * @return this
     */
    AbstractMessageBuilder insertWhenShiftClick(String insert);

    /**
     * 默認為 十分鐘 之後自動過期
     *
     * @param runner 運行函式
     * @return this
     */
    AbstractMessageBuilder run(Consumer<Player> runner);

    /**
     * @param timeoutClicks 點擊多少次後自動過期
     * @param runner        運行函式
     * @return this
     */
    AbstractMessageBuilder runClicks(int timeoutClicks, Consumer<Player> runner);

    /**
     * @param timeoutSeconds 多少秒後失效
     * @param runner         運行函式
     * @return this
     */
    AbstractMessageBuilder runTimeout(int timeoutSeconds, Consumer<Player> runner);

    /**
     * 隔行
     *
     * @return this
     */
    AbstractMessageBuilder nextLine();

    void sendPlayer(Player player);
}
