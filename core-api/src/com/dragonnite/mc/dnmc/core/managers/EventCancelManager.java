package com.dragonnite.mc.dnmc.core.managers;

import com.dragonnite.mc.dnmc.core.GetterFunction;
import org.bukkit.event.Event;

public interface EventCancelManager {

    /**
     * 註冊一個事件的獲取方式
     *
     * @param event  事件
     * @param type   獲取類別
     * @param getter 獲取定義
     * @param <T>    事件類
     * @param <R>    獲取類別
     */
    <T extends Event, R> void register(Class<T> event, Class<R> type, GetterFunction<T, R> getter);

}
