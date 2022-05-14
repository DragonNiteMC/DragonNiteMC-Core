package com.dragonite.mc.dnmc.core.config.yaml;


import com.dragonite.mc.dnmc.core.config.MessageGetter;

import java.util.List;

/**
 * 用於創建 訊息類別 config 時繼承
 */
public abstract class MessageConfiguration extends Configuration {

    private MessageGetter messageGetter;

    /**
     * 若果 訊息類別 config 本身沒有 @Prefix, 返回的將是 null
     *
     * @return 前綴
     */
    public String getPrefix() {
        return messageGetter.getPrefix();
    }

    /**
     * @param path 路徑
     * @return 獲取訊息
     */
    public String get(String path) {
        return messageGetter.get(path);
    }

    /**
     * @param path 路徑
     * @return 獲取無前綴訊息
     */
    public String getPure(String path) {
        return messageGetter.getPure(path);
    }

    /**
     * @param path 路徑
     * @return 獲取無前綴訊息列表
     */
    public List<String> getPureList(String path) {
        return messageGetter.getPureList(path);
    }

    /**
     * @param path 路徑
     * @return 獲取訊息列表
     */
    public List<String> getList(String path) {
        return messageGetter.getList(path);
    }
}
