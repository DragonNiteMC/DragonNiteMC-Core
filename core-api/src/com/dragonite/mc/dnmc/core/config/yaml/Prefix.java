package com.dragonite.mc.dnmc.core.config.yaml;

import java.lang.annotation.*;

/**
 * 用於 訊息類別 config 時標註 前綴 路徑
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Prefix {
    String path();
}
