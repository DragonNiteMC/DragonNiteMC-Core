package com.dragonite.mc.dnmc.core.managers;

import com.dragonite.mc.dnmc.core.config.yaml.Configuration;

/**
 * 新的 Config 管理器
 */
public interface YamlManager {

    /**
     * 重載所有文件
     *
     * @return 成功
     */
    boolean reloadConfigs();


    /**
     * @param yml 文件名稱
     * @param <T> 映射接口
     * @return 映射物件
     */
    <T extends Configuration> T getConfig(String yml);

    /**
     * @param config 映射物件類
     * @param <T>    映射接口
     * @return 映射物件
     */
    <T extends Configuration> T getConfigAs(Class<T> config);
}
