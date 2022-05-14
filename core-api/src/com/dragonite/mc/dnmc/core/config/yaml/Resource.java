package com.dragonite.mc.dnmc.core.config.yaml;

import java.lang.annotation.*;

/**
 * 用於創建 config class 時 定位 jar 內 yml 位置
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Resource {
    String locate();
}
