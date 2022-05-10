package com.dragonite.mc.dnmc.core.config.yaml;

import com.dragonite.mc.dnmc.core.config.FileController;

import java.io.IOException;

/**
 * 用於創建 Config class 時繼承
 */
public abstract class Configuration {

    private FileController controller;

    /**
     * 保存文件
     *
     * @throws IOException 保存失敗
     */
    public void save() throws IOException {
        controller.save(this);
    }

    /**
     * 重載文件
     */
    public void reload() {
        controller.reload(this);
    }
}
