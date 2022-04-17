package com.dragonnite.mc.dnmc.core.config;

import com.dragonnite.mc.dnmc.core.config.yaml.MessageConfiguration;

import java.util.List;

/**
 * 訊息獲取器
 *
 * @see MessageConfiguration
 */
public interface MessageGetter {

    String getPrefix();

    String get(String path);

    String getPure(String path);

    List<String> getList(String path);

    List<String> getPureList(String path);

}
