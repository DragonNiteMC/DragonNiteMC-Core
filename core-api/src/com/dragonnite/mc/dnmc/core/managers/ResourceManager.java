package com.dragonnite.mc.dnmc.core.managers;

import com.dragonnite.mc.dnmc.core.exception.PluginNotFoundException;
import com.dragonnite.mc.dnmc.core.exception.ResourceNotFoundException;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * 插件資源管理器
 */
public interface ResourceManager {

    /**
     * 插件資源類別
     */
    enum Type {
        /**
         * Spigot 插件
         */
        SPIGOT,
        /**
         * DragonNiteMC 專用插件
         */
        DRAGONNITE
    }

    /**
     * 獲取該插件的最新版本
     *
     * @param plugin 插件名稱
     * @return 版本名稱
     * @throws PluginNotFoundException 找不到插件
     * @throws ResourceNotFoundException 找不到遠端資源
     */
    String getLatestVersion(String plugin) throws PluginNotFoundException, ResourceNotFoundException;

    /**
     * 檢查該插件版本是否為最新版本
     *
     * @param plugin 插件名稱
     * @return 是否為最新版本
     * @throws PluginNotFoundException 找不到插件
     * @throws ResourceNotFoundException 找不到遠端資源
     */
    boolean isLatestVersion(String plugin) throws PluginNotFoundException, ResourceNotFoundException;

    /**
     * 刷新該插件的最新版本到快取
     * @param plugin 插件名稱
     * @param afterRun 運行成功時
     * @param errorRun 出現錯誤時
     */
    void fetchLatestVersion(String plugin, Consumer<String> afterRun, Consumer<Exception> errorRun);

    /**
     * 下載該插件的最新版本
     *
     * @param plugin 插件名稱
     * @return 插件檔案, 找不到遠端資源時為 null
     * @throws PluginNotFoundException 找不到插件
     */
    CompletableFuture<File> downloadLatestVersion(String plugin) throws PluginNotFoundException;

}
