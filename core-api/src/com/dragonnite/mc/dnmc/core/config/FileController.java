package com.dragonnite.mc.dnmc.core.config;

import com.dragonnite.mc.dnmc.core.config.yaml.Configuration;

import java.io.IOException;

/**
 * 文件控制器
 *
 * @see Configuration
 */
public interface FileController {

    <T extends Configuration> void save(T config) throws IOException;

    <T extends Configuration> void reload(T config);

}
